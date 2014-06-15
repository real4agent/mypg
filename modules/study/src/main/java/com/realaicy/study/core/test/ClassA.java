package com.realaicy.study.core.test;

/**
 * Created by realaicy on 2014/6/2.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 2014/6/2 17:45
 * @description TODO
 * @since TODO
 */
public class ClassA {

    public void testA(){
        System.out.println("In ClassA : testA");
        testdep();
    }

    public void testdep(){
        System.out.println("In ClassA : testdep");
    }
}
