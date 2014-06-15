package com.realaicy.study.map;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by realaicy on 2014/6/13.
 *
 * @author realaicy
 * @version TODO
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 2014/6/13 17:28
 * @description TODO
 * @since TODO
 */
public class MapTest {

    public static void main(String[] args) {

    }

    /**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    public static void randomHashMap(int min, int max, int n, ConcurrentHashMap<Integer, String> map) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            map.put(num, String.valueOf(System.currentTimeMillis()));
            //set.add(num);// 将不同的数存入HashSet中
        }
        int mapSize = map.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (mapSize < n) {
            randomHashMap(min, max, n - mapSize, map);// 递归
        }
    }
}
