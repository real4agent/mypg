package com.realaicy.pg.extra.debug.interceptor;

import com.realaicy.pg.core.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by realaicy on 2014/6/16.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 2014/6/16 7:10
 * @description TODO
 * @since TODO
 */
public class TraceRequest extends HandlerInterceptorAdapter {

    public static final Logger log = LoggerFactory.getLogger("pg-debug-request");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("###################################################################################");
        log.debug("===================== My Var begin==========================");
        try {
            log.debug(SecurityUtils.getSubject().getPrincipal().toString());
        } catch (Exception e) {

        }

        log.debug("===================== My Var  end==========================");
        log.debug("=====================request begin==========================");
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            uri = uri + "?" + queryString;
        }
        log.debug("{}:{}", request.getMethod(), uri);
        log.debug("remote ip:{}  sessionId:{}  ", IpUtils.getIpAddr(request), request.getRequestedSessionId());
        log.debug("===header begin============================================");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = headersToString(request.getHeaders(name));
            log.debug("{}={}", name, value);
        }
        log.debug("===header   end============================================");
        log.debug("===parameter begin==========================================");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = StringUtils.join(request.getParameterValues(name), "||");
            log.debug("{}={}", name, value);
        }
        log.debug("===parameter   end==========================================");
        log.debug("=====================request   end==========================");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        if (modelAndView != null)
            log.debug("view name is:{}", modelAndView.getViewName());
        if (modelAndView.getView() != null)
            log.debug("view name222 is:{}", modelAndView.getView().toString());

        if (handler != null)
            log.debug("handler name is:{}  ", handler.toString());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        if (handler != null)
            log.debug("handler name is:{}  ", handler.toString());
        super.afterCompletion(request, response, handler, ex);
    }

    private String headersToString(Enumeration<String> headers) {
        StringBuilder s = new StringBuilder();
        while (headers.hasMoreElements()) {
            s.append(headers.nextElement());
        }
        return s.toString();
    }
}
