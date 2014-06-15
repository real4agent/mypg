package com.realaicy.pg.core.web.utils;

import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class ServletUtils {

    /**
     * 判断指定请求url是否以firstPrefix+lastPrefixes开头method请求的
     * 如当前请求url是/sample/create 则匹配firstPrefix:/sample lastPrefixed /create
     *
     * @param request 请求对象
     * @param method 请求的方法
     * @param firstPrefix 前缀
     * @param lastPrefixes 后缀
     * @return 判断指定请求url是否以method请求的
     */
    public static boolean startWith(HttpServletRequest request, RequestMethod method,
                                    String firstPrefix, String... lastPrefixes) {

        String requestMethod = request.getMethod();
        if (!requestMethod.equalsIgnoreCase(method.name())) {
            return false;
        }
        String url = request.getServletPath();
        if (!url.startsWith(firstPrefix)) {
            return false;
        }

        if (lastPrefixes.length == 0) {
            return true;
        }

        url = url.substring(firstPrefix.length());
        for (String lastPrefix : lastPrefixes) {
            if (url.startsWith(lastPrefix)) {
                return true;
            }
        }
        return false;
    }
}
