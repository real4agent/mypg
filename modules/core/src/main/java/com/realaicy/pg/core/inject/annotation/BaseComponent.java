package com.realaicy.pg.core.inject.annotation;

import java.lang.annotation.*;

/**
 * 查找注解的字段作为BaseService/BaseRepository数据
 * <p/>即 查找对象中的注解了@BaseComponent的对象：然后
 * <br/>
 * 1.调用BaseCRUDController.setBaseService 设置BaseService
 * <br/>
 * 2.调用BaseService.setBaseRepository 设置BaseRepository
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseComponent {

}
