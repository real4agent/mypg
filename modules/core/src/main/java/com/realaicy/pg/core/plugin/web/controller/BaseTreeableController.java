package com.realaicy.pg.core.plugin.web.controller;

import com.google.common.collect.Lists;
import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.entity.BaseEntity;
import com.realaicy.pg.core.entity.enums.BooleanEnum;
import com.realaicy.pg.core.entity.search.SearchOperator;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.plugin.entity.Treeable;
import com.realaicy.pg.core.plugin.serivce.BaseTreeableService;
import com.realaicy.pg.core.plugin.web.controller.entity.ZTree;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.core.web.controller.BaseController;
import com.realaicy.pg.core.web.controller.permission.PermissionList;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public abstract class BaseTreeableController<M extends BaseEntity<ID> & Treeable<ID>, ID extends Serializable>
        extends BaseController<M, ID> {

    protected PermissionList permissionList = null;
    private BaseTreeableService<M, ID> treeableService;

    /**
     * 权限前缀：如sys:user
     * 则生成的新增权限为 sys:user:create
     */
    public void setResourceIdentity(String resourceIdentity) {
        if (!StringUtils.isEmpty(resourceIdentity)) {
            permissionList = PermissionList.newPermissionList(resourceIdentity);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        Assert.isTrue(
                BaseTreeableService.class.isAssignableFrom(baseService.getClass()),
                "baseService must be BaseTreeableService subclass");

        this.treeableService = (BaseTreeableService<M, ID>) this.baseService;
    }

    @RequestMapping(value = {"", "main"}, method = RequestMethod.GET)
    public String main() {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        return viewName("main");
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    public String tree(
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            Searchable searchable, Model model) {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        List<M> models;

        if (!StringUtils.isEmpty(searchName)) {
            searchable.addSearchParam("name_like", searchName);
            models = treeableService.findAllByName(searchable, null);
            if (!async) { //非异步 查自己和子子孙孙
                searchable.removeSearchFilter("name_like");
                List<M> children = treeableService.findChildren(models, searchable);
                models.removeAll(children);
                models.addAll(children);
            } else { //异步模式只查自己

            }
        } else {
            if (!async) {  //非异步 查自己和子子孙孙
                models = treeableService.findAllWithSort(searchable);
            } else {  //异步模式只查根 和 根一级节点
                models = treeableService.findRootAndChild(searchable);
            }
        }

        model.addAttribute("trees",
                convertToZtreeList(
                        models,
                        async,
                        true));

        return viewName("tree");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") M m, Model model) {
        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        setCommonData(model);
        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "查看");
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") M m, Model model, RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        if (m == null) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "您修改的数据不存在！");
            return redirectToUrl(viewName("success"));
        }

        setCommonData(model);
        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "修改");
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
            Model model,
            @ModelAttribute("m") M m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        if (result.hasErrors()) {
            return updateForm(m, model, redirectAttributes);
        }

        treeableService.update(m);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        return redirectToUrl(viewName("success"));
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("id") M m, Model model) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        setCommonData(model);
        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "删除");
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String deleteSelfAndChildren(
            Model model,
            @ModelAttribute("m") M m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        if (m.isRoot()) {
            result.reject("您删除的数据中包含根节点，根节点不能删除");
            return deleteForm(m, model);
        }

        treeableService.deleteSelfAndChild(m);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(viewName("success"));
    }

    @RequestMapping(value = "batch/delete")
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) ID[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        //如果要求不严格 此处可以删除判断 前台已经判断过了
        Searchable searchable = Searchable.newSearchable().addSearchFilter("id", SearchOperator.in, ids);
        List<M> mList = baseService.findAllWithNoPageNoSort(searchable);
        for (M m : mList) {
            if (m.isRoot()) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "您删除的数据中包含根节点，根节点不能删除");
                return redirectToUrl(backURL);
            }
        }

        treeableService.deleteSelfAndChild(mList);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "{parent}/appendChild", method = RequestMethod.GET)
    public String appendChildForm(Model model) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        setCommonData(model);
        if (!model.containsAttribute("child")) {
            model.addAttribute("child", newModel());
        }

        model.addAttribute(Constants.OP_NAME, "添加子节点");

        return viewName("appendChildForm");
    }

    @RequestMapping(value = "{parent}/appendChild", method = RequestMethod.POST)
    public String appendChild(
            Model model,
            @PathVariable("parent") M parent,
            @ModelAttribute("child") M child, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        setCommonData(model);

        if (result.hasErrors()) {
            return appendChildForm(model);
        }

        treeableService.appendChild(parent, child);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "添加子节点成功");
        return redirectToUrl(viewName("success"));
    }

    @RequestMapping(value = "{source}/move", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    public String showMoveForm(
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            @PathVariable("source") M source,
            Searchable searchable,
            Model model) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        List<M> models;

        //排除自己及子子孙孙
        searchable.addSearchFilter("id", SearchOperator.ne, source.getId());
        searchable.addSearchFilter(
                "parentIds",
                SearchOperator.notLike,
                source.makeSelfAsNewParentIds() + "%");

        if (!async) {
            models = treeableService.findAllWithSort(searchable);
        } else {
            models = treeableService.findRootAndChild(searchable);
        }

        model.addAttribute("trees", convertToZtreeList(
                models,
                async,
                true));

        model.addAttribute(Constants.OP_NAME, "移动节点");

        return viewName("moveForm");
    }

    @RequestMapping(value = "{source}/move", method = RequestMethod.POST)
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    public String move(
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            @PathVariable("source") M source,
            @RequestParam("target") M target,
            @RequestParam("moveType") String moveType,
            Searchable searchable,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        if (target.isRoot() && !moveType.equals("inner")) {
            model.addAttribute(Constants.ERROR, "不能移动到根节点之前或之后");
            return showMoveForm(async, source, searchable, model);
        }

        treeableService.move(source, target, moveType);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "移动节点成功");
        return redirectToUrl(viewName("success"));
    }

    @RequestMapping(value = "{parent}/children", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    public String list(
            @PathVariable("parent") M parent,
            Searchable searchable, Model model) throws UnsupportedEncodingException {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        if (parent != null) {
            searchable.addSearchFilter("parentId", SearchOperator.eq, parent.getId());
        }

        model.addAttribute("page", treeableService.findAll(searchable));

        return viewName("listChildren");
    }

    /**
     * 仅返回表格数据
     */
    @RequestMapping(value = "{parent}/children", headers = "table=true", method = RequestMethod.GET)
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    public String listTable(
            @PathVariable("parent") M parent,
            Searchable searchable, Model model) throws UnsupportedEncodingException {

        list(parent, searchable, model);
        return viewName("listChildrenTable");

    }

    @RequestMapping(value = "ajax/load")
    @PageableDefaults(sort = {"parentIds=asc", "weight=asc"})
    @ResponseBody
    public Object load(
            @RequestParam(value = "async", defaultValue = "true") boolean async,
            @RequestParam(value = "asyncLoadAll", defaultValue = "false") boolean asyncLoadAll,
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "id", required = false) ID parentId,
            @RequestParam(value = "excludeId", required = false) ID excludeId,
            @RequestParam(value = "onlyCheckLeaf", required = false, defaultValue = "false") boolean onlyCheckLeaf,
            Searchable searchable) {

        M excludeM = treeableService.findOne(excludeId);

        List<M> models;

        if (!StringUtils.isEmpty(searchName)) {//按name模糊查
            searchable.addSearchParam("name_like", searchName);
            models = treeableService.findAllByName(searchable, excludeM);
            if (!async || asyncLoadAll) {//非异步模式 查自己及子子孙孙 但排除
                searchable.removeSearchFilter("name_like");
                List<M> children = treeableService.findChildren(models, searchable);
                models.removeAll(children);
                models.addAll(children);
            } else { //异步模式 只查匹配的一级

            }
        } else { //根据有没有parentId加载

            if (parentId != null) { //只查某个节点下的 异步
                searchable.addSearchFilter("parentId", SearchOperator.eq, parentId);
            }

            if (async && !asyncLoadAll) { //异步模式下 且非异步加载所有
                //排除自己 及 子子孙孙
                treeableService.addExcludeSearchFilter(searchable, excludeM);

            }

            if (parentId == null && !asyncLoadAll) {
                models = treeableService.findRootAndChild(searchable);
            } else {
                models = treeableService.findAllWithSort(searchable);
            }
        }

        return convertToZtreeList(
                models,
                async && !asyncLoadAll && parentId != null,
                onlyCheckLeaf);
    }

    /////////////////////////////////////ajax///////////////////////////////////////////////

    @RequestMapping(value = "ajax/{parent}/appendChild", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxAppendChild(@PathVariable("parent") M parent) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        M child = newModel();
        child.setName("新节点");
        treeableService.appendChild(parent, child);
        return convertToZtree(child, true, true);
    }

    @RequestMapping(value = "ajax/{id}/delete", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxDeleteSelfAndChildren(@PathVariable("id") ID id) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        M tree = treeableService.findOne(id);
        treeableService.deleteSelfAndChild(tree);
        return tree;
    }

    @RequestMapping(value = "ajax/{id}/rename", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxRename(@PathVariable("id") M tree, @RequestParam("newName") String newName) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        tree.setName(newName);
        treeableService.update(tree);
        return convertToZtree(tree, true, true);
    }

    @RequestMapping(value = "ajax/{sourceId}/{targetId}/{moveType}/move", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxMove(
            @PathVariable("sourceId") M source, @PathVariable("targetId") M target,
            @PathVariable("moveType") String moveType) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        treeableService.move(source, target, moveType);

        return source;
    }

    @RequestMapping("ajax/autocomplete")
    @PageableDefaults(value = 30)
    @ResponseBody
    public Set<String> autocomplete(
            Searchable searchable,
            @RequestParam("term") String term,
            @RequestParam(value = "excludeId", required = false) ID excludeId) {

        return treeableService.findNames(searchable, term, excludeId);
    }

    @RequestMapping(value = "success")
    public String success() {
        return viewName("success");
    }

    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
    }

    @Override
    protected String redirectToUrl(String backURL) {
        if (!StringUtils.isEmpty(backURL)) {
            return super.redirectToUrl(backURL);
        }
        return super.redirectToUrl(viewName("success"));
    }

    @SuppressWarnings("unchecked")
    private List<ZTree<ID>> convertToZtreeList(List<M> models, boolean async, boolean onlySelectLeaf) {
        List<ZTree<ID>> zTrees = Lists.newArrayList();

        if (models == null || models.isEmpty()) {
            return zTrees;
        }

        for (M m : models) {
            ZTree zTree = convertToZtree(m, !async, onlySelectLeaf);
            zTrees.add(zTree);
        }
        return zTrees;
    }

    private ZTree convertToZtree(M m, boolean open, boolean onlyCheckLeaf) {
        ZTree<ID> zTree = new ZTree<ID>();
        zTree.setId(m.getId());
        zTree.setpId(m.getParentId());
        zTree.setName(m.getName());
        zTree.setIconSkin(m.getIcon());
        zTree.setOpen(open);
        zTree.setRoot(m.isRoot());
        zTree.setIsParent(m.isHasChildren());

        if (onlyCheckLeaf && zTree.isIsParent()) {
            zTree.setNocheck(true);
        } else {
            zTree.setNocheck(false);
        }

        return zTree;
    }

}



