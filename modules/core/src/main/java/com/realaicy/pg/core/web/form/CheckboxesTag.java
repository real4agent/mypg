package com.realaicy.pg.core.web.form;

import com.realaicy.pg.core.web.form.bind.SearchBindStatus;
import org.springframework.web.servlet.support.BindStatus;

import javax.servlet.jsp.JspException;

/**
 * 取值时
 * 1、先取parameter
 * 2、如果找不到再找attribute (page--->request--->session--->application)
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-3-24 上午9:21
 * @description xxx
 * @since 1.1
 */
public class CheckboxesTag extends org.springframework.web.servlet.tags.form.CheckboxesTag {

    private BindStatus bindStatus = null;

    @Override
    public void doFinally() {
        super.doFinally();
        this.bindStatus = null;
    }

    @Override
    protected BindStatus getBindStatus() throws JspException {
        if (this.bindStatus == null) {
            this.bindStatus = SearchBindStatus.create(pageContext, getName(), getRequestContext(), false);
        }
        return this.bindStatus;
    }

    @Override
    protected String getPropertyPath() throws JspException {
        return getPath();
    }

}
