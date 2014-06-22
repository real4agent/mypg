package com.realaicy.pg.extra.aop;

import com.realaicy.pg.core.cache.BaseCacheAspect;
import com.realaicy.pg.sys.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 缓存及清理菜单缓存
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Component
@Aspect
public class ResourceMenuCacheAspect extends BaseCacheAspect {

    public ResourceMenuCacheAspect() {
        setCacheName("sys-menuCache");
    }

    @Before(value = "resourceServicePointcut() && resourceCacheEvictAllPointcut()")
    public void findRolesCacheableAdvice() throws Throwable {
        clear();
    }

    @Around(value = "resourceServicePointcut() && resourceCacheablePointcut(arg)", argNames = "pjp,arg")
    public Object findRolesCacheableAdvice(ProceedingJoinPoint pjp, User arg) throws Throwable {

        String key = menusKey(arg.getId());
        Object retVal = get(key);

        if (retVal != null) {
            log.debug("cacheName:{}, method:findRolesCacheableAdvice, hit key:{}", cacheName, key);
            return retVal;
        }
        log.debug("cacheName:{}, method:findRolesCacheableAdvice, miss key:{}", cacheName, key);

        retVal = pjp.proceed();

        put(key, retVal);

        return retVal;
    }

    public void evict(Long userId) {
        evict(menusKey(userId));
    }

    @Pointcut(value = "target(com.realaicy.pg.sys.resource.service.ResourceService)")
    private void resourceServicePointcut() {
    }

    @Pointcut(value = "execution(* save(..)) || execution(* update(..)) || execution(* delete(..))")
    private void resourceCacheEvictAllPointcut() {
    }

    @Pointcut(value = "execution(* findMenus(*)) && args(arg)", argNames = "arg")
    private void resourceCacheablePointcut(User arg) {
    }

    private String menusKey(Long userId) {
        String menusKeyPrefix = "menus-";
        return menusKeyPrefix + userId;
    }

}
