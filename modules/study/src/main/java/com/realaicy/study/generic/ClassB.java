package com.realaicy.study.generic;

/**
 * Created by realaicy on 14-6-1.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-6-1 下午4:33
 * @description TODO
 * @since TODO
 */
public class ClassB<T, R, H> extends ClassA<T, R> {
    public ClassB(T obj, R realObj) {
        super(obj, realObj);
    }

    public ClassB(T obj, R realObj, H classBObj) {
        super(obj, realObj);
        this.classBObj = classBObj;
    }

    public H getClassBObj() {
        return classBObj;
    }

    public void setClassBObj(H classBObj) {
        this.classBObj = classBObj;
    }

    private H classBObj;

    public void echoAll() {
        System.out.println("T:" + getObject() + "; R:" + getRealObj() + "; B:" + getClassBObj());
    }
}
