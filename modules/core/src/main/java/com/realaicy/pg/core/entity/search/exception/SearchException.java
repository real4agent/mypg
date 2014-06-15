package com.realaicy.pg.core.entity.search.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 查找异常基类
 * <p/>
 * Created by realaicy on 14-3-24.
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class SearchException extends NestedRuntimeException {

    public SearchException(String msg) {
        super(msg);
    }

    public SearchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
