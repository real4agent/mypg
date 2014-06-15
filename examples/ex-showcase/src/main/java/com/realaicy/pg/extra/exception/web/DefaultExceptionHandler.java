package com.realaicy.pg.extra.exception.web;

import com.realaicy.pg.core.utils.LogUtils;
import com.realaicy.pg.extra.exception.web.entity.ExceptionResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>默认异常处理</p>
 * <p/>
 * Created by realaicy on 14-3-24.
 *
 * @author <a href="mailto:realaicy@gmail.com">realaicy</a>
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-3-24 上午9:21
 * @description xxx
 * @since 1.1
 */
@ControllerAdvice
public class DefaultExceptionHandler {

    /**
     * 权限异常处理
     * 所有的被扫描的Request Mapping 都适用
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {

        LogUtils.logError("用户权限验证失败", e);

        ExceptionResponse exceptionResponse = ExceptionResponse.from(e);

        ModelAndView mv = new ModelAndView();
        mv.addObject("error", exceptionResponse);
        mv.setViewName("error/exception");

        return mv;
    }
}
