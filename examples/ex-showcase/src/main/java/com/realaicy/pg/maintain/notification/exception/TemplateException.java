package com.realaicy.pg.maintain.notification.exception;

import com.realaicy.pg.core.exception.BaseException;

/**
 * 异常 ：模板模块异常
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class TemplateException extends BaseException {

    public TemplateException(final String code, final Object[] args) {
        super("notification", code, args);
    }
}
