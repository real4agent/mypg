package com.realaicy.pg.core.utils;

/**
 * 重做一个毫秒级的简单StopWatch轮子 ^_^。
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class StopWatch {
    private long startTime;

    public StopWatch() {
        startTime = System.currentTimeMillis();
    }

    public long getMillis() {
        return System.currentTimeMillis() - startTime;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }
}
