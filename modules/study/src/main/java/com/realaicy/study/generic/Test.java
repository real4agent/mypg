package com.realaicy.study.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by realaicy on 14-6-1.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-6-1 下午4:40
 * @description TODO
 * @since TODO
 */
public class Test {
    public static void main(String[] args) {
        ClassA<String,Integer> clazzA  = new ClassA<String,Integer>("AAA",111);
        ClassB<String,Integer,Integer> clazzB  = new ClassB<String,Integer,Integer>("AAA",111,222);
        System.out.println(clazzA.getClass().getGenericSuperclass());
        System.out.println(clazzB.getClass().getGenericSuperclass());
        Type[] actualTypeArguments = ((ParameterizedType) (clazzB.getClass().getGenericSuperclass())).getActualTypeArguments();
        System.out.println(actualTypeArguments[1]);
        //System.out.println(ReflectUtils.findParameterizedType(clazzB.getClass(), 0));
    }
}
