/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.personal.message.exception;

import com.realaicy.pg.core.exception.BaseException;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-5-24 上午7:22
 * <p>Version: 1.0
 */
public class MessageException extends BaseException {

    public MessageException(String code, Object[] args) {
        super("personal", code, args);
    }
}
