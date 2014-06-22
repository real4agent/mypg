package com.realaicy.pg.maintain.keyvalue.web.controller;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.core.web.validate.ValidateResponse;
import com.realaicy.pg.maintain.keyvalue.entity.KeyValue;
import com.realaicy.pg.maintain.keyvalue.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SD-JPA-Controller：keyValue
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
@RequestMapping(value = "/admin/maintain/keyvalue")
public class KeyValueController extends BaseCRUDController<KeyValue, Long> {

    @Autowired
    @BaseComponent
    private KeyValueService keyValueService;

    public KeyValueController() {
        setResourceIdentity("maintain:icon");
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

        if ("key".equals(fieldId)) {
            KeyValue keyValue = keyValueService.findByKey(fieldValue);
            if (keyValue == null || (keyValue.getId().equals(id) && keyValue.getKey().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该键已被使用");
            }
        }
        return response.result();
    }

}
