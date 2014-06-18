package com.realaicy.pg.core.web.controller;

import com.realaicy.pg.core.entity.AbstractEntity;
import com.realaicy.pg.core.inject.support.InjectBaseDependencyHelper;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.core.utils.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;

/**
 * 基础控制器
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public abstract class BaseController<M extends AbstractEntity, ID extends Serializable>
        implements InitializingBean {

    /**
     * 实体类型
     */
    protected final Class<M> entityClass;
    protected BaseService<M, ID> baseService;
    private String viewPrefix;

    /**
     * 默认构造函数
     * 首先通过反射获得当前Controller操作的具体的实体的类型
     * 接着设置视图的默认前缀
     */
    protected BaseController() {
        this.entityClass = ReflectionUtils.findParameterizedType(getClass(), 0);
        setViewPrefix(defaultViewPrefix());
    }

    /**
     * 设置基础service
     */
    public void setBaseService(BaseService<M, ID> baseService) {
        this.baseService = baseService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.entityClass != null) {
            InjectBaseDependencyHelper.findAndInjectBaseServiceDependency(this);
            Assert.notNull(baseService, "BaseService required, Class is:" + getClass());
        }
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        if (viewPrefix.startsWith("/")) {
            viewPrefix = viewPrefix.substring(1);
        }
        this.viewPrefix = viewPrefix;
    }

    /**
     * 获取视图名称：即prefixViewName + "/" + suffixName
     *
     * @return 视图名称
     */
    public String viewName(String suffixName) {
        if (!suffixName.startsWith("/")) {
            suffixName = "/" + suffixName;
        }
        return getViewPrefix() + suffixName;
    }

    /**
     * 设置通用数据
     */
    protected void setCommonData(Model model) {
    }

    protected M newModel() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("can not instantiated model : " + this.entityClass, e);
        }
    }

    /**
     * 共享的验证规则
     * 验证失败返回true
     */
    protected boolean hasError(M m, BindingResult result) {
        Assert.notNull(m);
        return result.hasErrors();
    }

    /**
     * @param backURL Url,如果为null 将重定向到默认getViewPrefix()
     * @return "redirect:" + backURL
     */
    protected String redirectToUrl(String backURL) {
        if (StringUtils.isEmpty(backURL)) {
            backURL = getViewPrefix();
        }
        if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
            backURL = "/" + backURL;
        }
        return "redirect:" + backURL;
    }

    /**
     * 获取默认的视图前缀当前模块 视图的前缀
     * 默认
     * 1、获取当前类头上的@RequestMapping中的value作为前缀
     * 2、如果没有就使用当前模型小写的简单类名
     */
    protected String defaultViewPrefix() {
        String currentViewPrefix = "";
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            currentViewPrefix = requestMapping.value()[0];
        }

        if (StringUtils.isEmpty(currentViewPrefix)) {
            currentViewPrefix = this.entityClass.getSimpleName();
        }
        return currentViewPrefix;
    }
}
