package com.realaicy.pg.core.web.bind.annotation;

import java.lang.annotation.*;

/**
 * <p>绑定请求参数到模型，并且暴露到模型中供页面使用</p>
 * <p>不同于@ModelAttribute</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormModel {

    /**
     * 指定请求参数的前缀和暴露到模型对象的名字供视图使用
     * <p/>
     * <p>1、绑定请求参数到模型，绑定规则<br/>
     * 如请求表单：<br>
     * <pre class="code">
     * <input name="student.name" value="Kate" /><br>
     * <input name="student.type" value="自费" /><br>
     * </pre>
     * 则请求处理方法：<br>
     * <pre class="code">
     * <p/>
     * "@RequestMapping(value = "/test")"
     * public String test(@FormModel("student") Student student) //这样将绑定  student.name student.type两个参数
     * </pre>
     * <p/>
     * 而springmvc默认<br>
     * 如请求表单：<br>
     * <pre class="code">
     * <input name="name" value="Kate" /><br>
     * <input name="type" value="自费" /><br>
     * </pre>
     * 则请求处理方法：<br>
     * <pre class="code">
     * public String test(@ModelAttribute("student") Student student) //这样将绑定name type两个参数
     * </pre>
     * <p/>
     * <p>2、根据value中的名字暴露到模型对象中供视图使用
     */
    String value();

}
