package com.realaicy.pg.core.inject.support;

import com.google.common.collect.Sets;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.core.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 注入BaseRepository 及 BaseService 的工具类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class InjectBaseDependencyHelper {

    @SuppressWarnings("unchecked")
    public static void findAndInjectBaseRepositoryDependency(BaseService<?, ?> baseService) {

        final Set<Object> candidates =
                findDependencies(baseService, BaseComponent.class);
        if (candidates.size() == 0 || candidates.size() > 1) {
            throw new IllegalStateException(
                    "expect 1 @BaseComponent anntation BaseRepository subclass bean, but found " + candidates.size() +
                            ", please check class [" + baseService.getClass() + "] @BaseComponent annotation.");
        }

        Object baseRepository = candidates.iterator().next();

        if (baseRepository.getClass().isAssignableFrom(BaseComponent.class)) {
            throw new IllegalStateException("[" + baseService.getClass() + "] @BaseComponent annotation bean " +
                    "must be BaseRepository subclass");
        }
        baseService.setBaseRepository((BaseRepository) baseRepository);
    }

    @SuppressWarnings("unchecked")
    public static void findAndInjectBaseServiceDependency(BaseController<?, ?> baseController) {
        final Set<Object> candidates =
                findDependencies(baseController, BaseComponent.class);

        if (candidates.size() > 1) {
            throw new IllegalStateException(
                    "expect 1 @BaseComponent anntation BaseService subclass bean, but found " + candidates.size() +
                            ", please check class [" + baseController.getClass() + "] @BaseComponent annotation.");
        }

        Object baseService = candidates.iterator().next();

        if (baseService.getClass().isAssignableFrom(BaseComponent.class)) {
            throw new IllegalStateException("[" + baseController.getClass() + "] @BaseComponent annotation bean " +
                    "must be BaseService subclass");
        }

        baseController.setBaseService((BaseService) baseService);
    }

    /**
     * 根据注解在目标对象上的字段上查找依赖
     *
     * @param target 目标对象
     * @param annotation 需要查找的注解
     */
    private static Set<Object> findDependencies(final Object target, final Class<? extends Annotation> annotation) {

        final Set<Object> candidates = Sets.newHashSet();

        ReflectionUtils.doWithFields(
                target.getClass(),
                new ReflectionUtils.FieldCallback() {
                    @Override
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        ReflectionUtils.makeAccessible(field);
                        Object obj = ReflectionUtils.getField(field, target);
                        candidates.add(obj);
                    }
                },
                new ReflectionUtils.FieldFilter() {
                    @Override
                    public boolean matches(Field field) {
                        return field.isAnnotationPresent(annotation);
                    }
                }
        );

        ReflectionUtils.doWithMethods(
                target.getClass(),
                new ReflectionUtils.MethodCallback() {
                    @Override
                    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                        ReflectionUtils.makeAccessible(method);
                        PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                        candidates.add(ReflectionUtils.invokeMethod(descriptor.getReadMethod(), target));
                    }
                },
                new ReflectionUtils.MethodFilter() {
                    @Override
                    public boolean matches(Method method) {
                        if (!method.isAnnotationPresent(annotation))
                            return false;

                        PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                        // boolean hasReadMethod = false;
                        //hasReadMethod = descriptor != null && descriptor.getReadMethod() != null;

                        return descriptor != null && descriptor.getReadMethod() != null;
                    }
                }
        );
        return candidates;
    }
}
