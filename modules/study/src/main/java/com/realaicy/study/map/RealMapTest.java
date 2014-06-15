package com.realaicy.study.map;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by realaicy on 2014/6/13.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 2014/6/13 15:19
 * @description TODO
 * @since TODO
 */
public class RealMapTest {
    public static void main(String args[]) {

        //NavigableMap extends SortedMap to provide useful navigation methods
        NavigableMap<String, String> navigableMap = new TreeMap<String, String>();

        navigableMap.put("中国", "Good programming language");
        navigableMap.put("中国人民医院", "Another good programming language");
        navigableMap.put("北京", "Another JVM language");
        navigableMap.put("北京A医院", "Another JVM language");
        navigableMap.put("北京b医院", "Another JVM language");
        navigableMap.put("北京c医院", "Another JVM language");
        navigableMap.put("北京d医院", "Another JVM language");
        navigableMap.put("北京e医院", "Another JVM language");

        navigableMap.put("天津医院", "Another JVM language");
        navigableMap.put("天津A医院", "Another JVM language");
        navigableMap.put("天津b医院", "Another JVM language");
        navigableMap.put("天津c医院", "Another JVM language");
        navigableMap.put("天津d医院", "Another JVM language");
        navigableMap.put("天津e医院", "Another JVM language");


        navigableMap.put("Python", "Language which Google use");

        System.out.println("SorteMap : " + navigableMap);

        //lowerKey returns key which is less than specified key
        System.out.println("lowerKey from 中国 : " + navigableMap.lowerKey("中国"));
        System.out.println("higherKey from 中国: " + navigableMap.higherKey("中国"));

        System.out.println("lowerKey from 北京 : " + navigableMap.lowerKey("北京"));
        System.out.println("higherKey from 北京: " + navigableMap.higherKey("北京"));

//        //floorKey returns key which is less than or equal to specified key
//        System.out.println("floorKey from Java: " + navigableMap.floorKey("Java"));
//
//        //ceilingKey returns key which is greater than or equal to specified key
//        System.out.println("ceilingKey from Java: " + navigableMap.ceilingKey("Java"));
//
//        //higherKey returns key which is greater specified key
//        System.out.println("higherKey from Java: " + navigableMap.higherKey("Java"));
//
//        //Apart from navigagtion methodk, it also provides useful method
//        //to create subMap from existing Map e.g. tailMap, headMap and subMap
//
//        //an example of headMap - returns NavigableMap whose key is less than specified
//        NavigableMap<String, String> headMap = navigableMap.headMap("Python", false);
//        System.out.println("headMap created form navigableMap : " + headMap);
//
//        //an example of tailMap - returns NavigableMap whose key is greater than specified
//        NavigableMap<String, String> tailMap = navigableMap.tailMap("Scala", false);
//        System.out.println("tailMap created form navigableMap : " + tailMap);
//
//        //an example of subMap - return NavigableMap from toKey to fromKey
//        NavigableMap<String, String> subMap = navigableMap.subMap("C++", false,
//                "Python", false);
//        System.out.println("subMap created form navigableMap : " + subMap);
    }

}
