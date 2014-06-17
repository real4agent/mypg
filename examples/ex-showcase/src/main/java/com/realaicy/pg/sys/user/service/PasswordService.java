package com.realaicy.pg.sys.user.service;

import com.realaicy.pg.core.utils.security.Md5Utils;
import com.realaicy.pg.sys.user.entity.User;
import com.realaicy.pg.sys.user.exception.UserPasswordNotMatchException;
import com.realaicy.pg.sys.user.exception.UserPasswordRetryLimitExceedException;
import com.realaicy.pg.sys.user.utils.UserLogUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * SD-JPA-Service：密码服务
 * <p/>
 * 提供如下服务：<br/>
 * 1.判断用户和密码是否匹配
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Service
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);

    @Autowired
    private CacheManager ehcacheManager;

    private Cache loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount = 10;

    public static void main(String[] args) {
        System.out.println(new PasswordService().encryptPassword("monitor", "123456", "iY71e4d123"));
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @PostConstruct
    public void init() {
        loginRecordCache = ehcacheManager.getCache("loginRecordCache");
    }

    /**
     * 校验用户和密码是否匹配
     * <p/>
     * 首先判断校验失败的次数是否超过允许的最大值，如果超过则在相应的已配置的日志级别下记录日志，并且抛出相应异常
     * 如果没有超过，则判断这次用户和密码是否匹配，如果不匹配则在相应的已配置的日志级别下记录日志，并且抛出相应异常
     * 如果匹配则将校验记录（包括以前错误的）从缓存中清除
     *
     * @param user 用户
     * @param password 密码
     */
    public void validate(User user, String password) {

        String username = user.getUsername();
        int retryCount = 0;

        Element cacheElement = loginRecordCache.get(username);
        if (cacheElement != null) {
            retryCount = (Integer) cacheElement.getObjectValue();
            if (retryCount >= maxRetryCount) {
                UserLogUtils.log(
                        username,
                        "passwordError",
                        "password error, retry limit exceed! password: {},max retry count {}",
                        password, maxRetryCount);
                throw new UserPasswordRetryLimitExceedException(maxRetryCount);
            }
        }

        if (!matches(user, password)) {
            loginRecordCache.put(new Element(username, ++retryCount));
            UserLogUtils.log(
                    username,
                    "passwordError",
                    "password error! password: {} retry count: {}",
                    password, retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(User user, String newPassword) {
        log.debug("Mothod:matches-----------pw in db:{}\t---pw from user input:{}\t---pw cal:{}",
                user.getPassword(), newPassword, encryptPassword(user.getUsername(), newPassword, user.getSalt()));
        return user.getPassword().equals(encryptPassword(user.getUsername(), newPassword, user.getSalt()));
    }

    public void clearLoginRecordCache(String username) {
        loginRecordCache.remove(username);
    }

    public String encryptPassword(String username, String password, String salt) {
        return Md5Utils.hash(username + password + salt);
    }
}
