package com.realaicy.study.generic;

/**
 * Created by realaicy on 14-6-1.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-6-1 下午4:30
 * @description TODO
 * @since TODO
 */
public class ClassA<T, R> {
    private T obj;

    public ClassA(T obj, R realObj) {
        this.obj = obj;
        this.realObj = realObj;
    }

    private R realObj;

    public R getRealObj() {
        return realObj;
    }

    public void setRealObj(R realObj) {
        this.realObj = realObj;
    }

    public void setObject(T obj) {
        this.obj = obj;
    }

    public T getObject() {
        return obj;
    }
}
