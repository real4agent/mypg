package com.realaicy.study.core.test;

/**
 * Created by realaicy on 14-5-31.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-5-31 上午11:05
 * @description TODO
 * @since TODO
 */
public class StringTest {
    public static void main(String[] args) {
        String symbol = "    aa   bb   cc         d";
        String symbol2 = "> =";
        System.out.println(symbol2.trim().toLowerCase().replace("  ", " "));
        if (String.class.isAssignableFrom(Object.class)){
            System.out.println("aaaaaaaaaaaaaaaaaaaaa");
        }
    }
}
