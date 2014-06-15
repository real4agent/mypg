package com.realaicy.pg.core.plugin.web.controller;

import com.realaicy.pg.core.entity.BaseEntity;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.plugin.entity.Movable;
import com.realaicy.pg.core.plugin.serivce.BaseMovableService;
import com.realaicy.pg.core.utils.MessageUtils;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.core.web.validate.AjaxResponse;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public abstract class BaseMovableController<M extends BaseEntity & Movable, ID extends Serializable>
        extends BaseCRUDController<M, ID> {

    private BaseMovableService movableService;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        Assert.isTrue(
                BaseMovableService.class.isAssignableFrom(baseService.getClass()),
                "baseService must be BaseMovableService subclass");

        this.movableService = (BaseMovableService<M, ID>) this.baseService;

    }

    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(value = 10, sort = "weight=desc")
    @Override
    public String list(Searchable searchable, Model model) {

        return super.list(searchable, model);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "{fromId}/{toId}/up")
    @ResponseBody
    public AjaxResponse up(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        AjaxResponse ajaxResponse = new AjaxResponse("移动位置成功");
        try {
            movableService.up(fromId, toId);
        } catch (IllegalStateException e) {
            ajaxResponse.setSuccess(Boolean.FALSE);
            ajaxResponse.setMessage(MessageUtils.message("move.not.enough"));
        }
        return ajaxResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "{fromId}/{toId}/down")
    @ResponseBody
    public AjaxResponse down(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        AjaxResponse ajaxResponse = new AjaxResponse("移动位置成功");
        try {
            movableService.down(fromId, toId);
        } catch (IllegalStateException e) {
            ajaxResponse.setSuccess(Boolean.FALSE);
            ajaxResponse.setMessage(MessageUtils.message("move.not.enough"));
        }
        return ajaxResponse;
    }

    @RequestMapping(value = "reweight")
    @ResponseBody
    public AjaxResponse reweight() {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        AjaxResponse ajaxResponse = new AjaxResponse("优化权重成功！");
        try {
            movableService.reweight();
        } catch (IllegalStateException e) {
            ajaxResponse.setSuccess(Boolean.FALSE);
            ajaxResponse.setMessage("优化权重失败了！");
        }
        return ajaxResponse;
    }

}
