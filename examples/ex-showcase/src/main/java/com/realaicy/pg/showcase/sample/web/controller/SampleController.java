package com.realaicy.pg.showcase.sample.web.controller;

import com.realaicy.pg.core.entity.enums.BooleanEnum;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.core.web.validate.ValidateResponse;
import com.realaicy.pg.showcase.sample.entity.Sample;
import com.realaicy.pg.showcase.sample.entity.Sex;
import com.realaicy.pg.showcase.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * SD-JPA-Controller：样例
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
@RequestMapping(value = "/showcase/sample")
public class SampleController extends BaseCRUDController<Sample, Long> {

    @Autowired
    @BaseComponent
    private SampleService sampleService;

    public SampleController() {
        setResourceIdentity("showcase:sample");
    }

    /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     */
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        ValidateResponse response = ValidateResponse.newInstance();

        if ("name".equals(fieldId)) {
            Sample sample = sampleService.findByName(fieldValue);
            if (sample == null || (sample.getId().equals(id) && sample.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已被其他人使用");
            }
        }
        return response.result();
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("sexList", Sex.values());
        model.addAttribute("booleanList", BooleanEnum.values());
    }

    /**
     * 验证失败返回true
     */
    @Override
    protected boolean hasError(Sample m, BindingResult result) {
        Assert.notNull(m);

        //字段错误 前台使用<pg:showFieldError commandName="showcase/sample"/> 显示
        if (m.getBirthday() != null && m.getBirthday().after(new Date())) {
            //前台字段名（前台使用[name=字段名]取得dom对象） 错误消息键。。
            result.rejectValue("birthday", "birthday.past");
        }

        //全局错误 前台使用<pg:showGlobalError commandName="showcase/sample"/> 显示
        if (m.getName().contains("admin")) {
            result.reject("name.must.not.admin");
        }

        return result.hasErrors();
    }

}
