package com.realaicy.pg.core.web.validate;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 验证结果
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-3-24 上午9:21
 * @description TODO
 * @since 1.1
 */
public class ValidateResponse {
    /**
     * 验证成功
     */
    private static final Integer OK = 1;
    /**
     * 验证失败
     */
    private static final Integer FAIL = 0;

    private List<Object> results = Lists.newArrayList();

    private ValidateResponse() {
    }

    public static ValidateResponse newInstance() {
        return new ValidateResponse();
    }

    /**
     * 验证失败（使用前台alertTextOk定义的消息）
     *
     * @param fieldId 验证失败的字段名
     */
    public void validateFail(String fieldId) {
        
        validateFail(fieldId, "");
    }

    /**
     * 验证失败
     *
     * @param fieldId 验证失败的字段名
     * @param message 验证失败时显示的消息
     */
    public void validateFail(String fieldId, String message) {

        results.add(new Object[]{fieldId, FAIL, message});
    }

    /**
     * 验证成功（使用前台alertTextOk定义的消息）
     *
     * @param fieldId 验证成功的字段名
     */
    public void validateSuccess(String fieldId) {

        validateSuccess(fieldId, "");
    }

    /**
     * 验证成功
     *
     * @param fieldId 验证成功的字段名
     * @param message 验证成功时显示的消息
     */
    public void validateSuccess(String fieldId, String message) {

        results.add(new Object[]{fieldId, OK, message});
    }

    /**
     * 返回验证结果
     */
    public Object result() {

        if (results.size() == 1) {
            return results.get(0);
        }
        return results;
    }

}
