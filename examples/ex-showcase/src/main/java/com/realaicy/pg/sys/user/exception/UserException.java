package com.realaicy.pg.sys.user.exception;

import com.realaicy.pg.core.exception.BaseException;

/**
 * 异常类：用户模块异常基类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class UserException extends BaseException {

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}
