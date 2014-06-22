package com.realaicy.pg.maintain.notification.web.controller;

import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.core.web.bind.annotation.SearchableDefaults;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.core.web.validate.ValidateResponse;
import com.realaicy.pg.maintain.notification.entity.NotificationSystem;
import com.realaicy.pg.maintain.notification.entity.NotificationTemplate;
import com.realaicy.pg.maintain.notification.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SD-JPA-Controller：通知模板
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
@RequestMapping(value = "/admin/maintain/notification/template")
public class NotificationTemplateController extends BaseCRUDController<NotificationTemplate, Long> {

    @Autowired
    @BaseComponent
    private NotificationTemplateService notificationTemplateService;

    public NotificationTemplateController() {
        setResourceIdentity("maintain:notificationTemplate");
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    @SearchableDefaults(value = "deleted_eq=true", merge = true)
    public String list(Searchable searchable, Model model) {
        return super.list(searchable, model);
    }

    /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     *
     * @param fieldId xxx
     * @param fieldValue xxx
     * @return xxx
     */
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        ValidateResponse response = ValidateResponse.newInstance();

        if ("name".equals(fieldId)) {
            NotificationTemplate template = notificationTemplateService.findByName(fieldValue);
            if (template == null || (template.getId().equals(id) && template.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已被其他模板使用");
            }
        }
        return response.result();
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("notificationSystems", NotificationSystem.values());
    }

    /**
     * 验证失败返回true
     *
     * @param m xxx
     * @param result xxx
     * @return xxx
     */
    @Override
    protected boolean hasError(NotificationTemplate m, BindingResult result) {
        Assert.notNull(m);

        NotificationTemplate template = notificationTemplateService.findByName(m.getName());
        if (template == null || (template.getId().equals(m.getId()) && template.getName().equals(m.getName()))) {
            //success
        } else {
            result.rejectValue("name", "该名称已被其他模板使用");
        }

        return result.hasErrors();
    }

}
