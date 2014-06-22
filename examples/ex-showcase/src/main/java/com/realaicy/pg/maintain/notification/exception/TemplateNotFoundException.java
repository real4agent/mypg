package com.realaicy.pg.maintain.notification.exception;

/**
 * 异常: 模板没有找到
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class TemplateNotFoundException extends TemplateException {
    public TemplateNotFoundException(String templateName) {
        super("notification.template.not.found", new Object[]{templateName});
    }
}
