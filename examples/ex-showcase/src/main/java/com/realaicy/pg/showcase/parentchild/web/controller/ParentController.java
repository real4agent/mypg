package com.realaicy.pg.showcase.parentchild.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.entity.enums.BooleanEnum;
import com.realaicy.pg.core.entity.search.SearchOperator;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.bind.annotation.FormModel;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.showcase.parentchild.entity.Child;
import com.realaicy.pg.showcase.parentchild.entity.Parent;
import com.realaicy.pg.showcase.parentchild.entity.ParentChildType;
import com.realaicy.pg.showcase.parentchild.service.ChildService;
import com.realaicy.pg.showcase.parentchild.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * SD-JPA-Controller：父亲
 * <p/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Controller
@RequestMapping(value = "/showcase/parentchild/parent")
public class ParentController extends BaseCRUDController<Parent, Long> {

    @Autowired
    @BaseComponent
    private ParentService parentService;

    @Autowired
    private ChildService childService;

    protected ParentController() {
        setListAlsoSetCommonData(true);
        setResourceIdentity("showcase:parentchild");
    }

    @RequestMapping(value = "create/discard", method = RequestMethod.POST)
    @Override
    public String create(Model model, @Valid @ModelAttribute("m") Parent parent,
                         BindingResult result, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(
            Model model,
            @Valid @ModelAttribute("parent") Parent parent, BindingResult result,
            @FormModel("childList") List<Child> childList,
            RedirectAttributes redirectAttributes) {

        if (hasError(parent, result)) {
            return showCreateForm(model);
        }
        parentService.save(parent, childList);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建成功");
        return redirectToUrl(null);
    }

    @Override
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Parent parent, Model model) {
        model.addAttribute("childList", childService.findByParent(parent, null).getContent());
        return super.showUpdateForm(parent, model);
    }

    @RequestMapping(value = "{id}/update/discard", method = RequestMethod.POST)
    @Override
    public String update(Model model, @Valid @ModelAttribute("m") Parent parent, BindingResult result, @RequestParam(value = "BackURL", required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute("parent") Parent parent, BindingResult result,
            @FormModel("childList") List<Child> childList,
            @RequestParam(value = "BackURL", required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (hasError(parent, result)) {
            return showUpdateForm(parent, model);
        }
        parentService.update(parent, childList);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    @Override
    public String showDeleteForm(@PathVariable("id") Parent parent, Model model) {
        model.addAttribute("childList", childService.findByParent(parent, null).getContent());
        return super.showDeleteForm(parent, model);
    }

    @RequestMapping(value = "{parent}/child", method = RequestMethod.GET)
    @PageableDefaults(value = Integer.MAX_VALUE, sort = "id=desc")
    public String listChild(Model model, @PathVariable("parent") Long parentId, Searchable searchable) {

        this.permissionList.assertHasViewPermission();

        searchable.addSearchFilter("parent.id", SearchOperator.eq, parentId);

        model.addAttribute("page", childService.findAll(searchable));

        return "showcase/parentchild/child/list";
    }

    @RequestMapping(value = "child/create", method = RequestMethod.GET)
    public String showChildCreateForm(Model model) {

        this.permissionList.assertHasEditPermission();

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("child")) {
            model.addAttribute("child", new Child());
        }
        return "showcase/parentchild/child/editForm";
    }

    //////////////////////////////////child////////////////////////////////////

    @RequestMapping(value = "child/{id}/update", method = RequestMethod.GET)
    public String showChildUpdateForm(
            Model model,
            @PathVariable("id") Child child,
            @RequestParam(value = "copy", defaultValue = "false") boolean isCopy) {

        this.permissionList.assertHasEditPermission();

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, isCopy ? "复制" : "修改");
        if (!model.containsAttribute("child")) {
            if (child == null) {
                child = new Child();
            }
            if (isCopy) {
                child.setId(null);
            }
            model.addAttribute("child", child);
        }
        return "showcase/parentchild/child/editForm";
    }

    @RequestMapping(value = "child/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Child deleteChild(@PathVariable("id") Child child) {

        this.permissionList.assertHasEditPermission();

        childService.delete(child);
        return child;
    }

    @RequestMapping(value = "child/batch/delete")
    @ResponseBody
    public Object deleteChildInBatch(@RequestParam(value = "ids", required = false) Long[] ids) {

        this.permissionList.assertHasEditPermission();

        childService.delete(ids);
        return ids;
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
        model.addAttribute("typeList", ParentChildType.values());
    }

    /**
     * 验证失败返回true
     */
    protected boolean hasError(Parent parent, BindingResult result) {
        Assert.notNull(parent);

        //全局错误 前台使用<pg:showGlobalError commandName="showcase/parent"/> 显示
        if (parent.getName().contains("admin")) {
            result.reject("name.must.not.admin");
        }

        return result.hasErrors();
    }

}
