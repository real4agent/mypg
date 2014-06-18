package com.realaicy.pg.personal.message.exception;

import com.realaicy.pg.core.exception.BaseException;

/**
 * 异常：消息模块异常
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class MessageException extends BaseException {

    public MessageException(String code, Object[] args) {
        super("personal", code, args);
    }
}
