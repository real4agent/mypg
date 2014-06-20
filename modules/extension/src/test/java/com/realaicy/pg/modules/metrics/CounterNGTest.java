package com.realaicy.pg.modules.metrics;

import com.realaicy.pg.modules.metrics.utils.Clock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class CounterNGTest {

    private Clock.MockClock clock = new Clock.MockClock();

    @BeforeMethod
    public void setup() {
        Counter.clock = clock;
    }

    @Test
    public void normal() {

        Counter counter = new Counter();
        counter.inc(10);
        counter.inc(20);
        counter.inc(30);
        clock.increaseTime(1000);

        CounterMetric metric = counter.calculateMetric();
        assertEquals(60, metric.totalCount);
        assertEquals(60, metric.lastCount);
        assertEquals(60d, metric.lastRate, 0);

        counter.inc(20);
        clock.increaseTime(1000);
        metric = counter.calculateMetric();

        assertEquals(80, metric.totalCount);
        assertEquals(20, metric.lastCount);
        assertEquals(20d, metric.lastRate, 0);

        counter.inc(66);
        clock.increaseTime(1600);
        metric = counter.calculateMetric();

        assertEquals(146, metric.totalCount);
        assertEquals(66, metric.lastCount);
        assertEquals(41d, metric.lastRate, 0);
    }

    @Test
    public void incAndDec() {
        Counter counter = new Counter();

        counter.inc(20);
        counter.inc();
        counter.inc();
        counter.dec(10);
        counter.dec();
        clock.increaseTime(1000);

        CounterMetric metric = counter.calculateMetric();
        assertEquals(11, metric.totalCount);
    }

    @Test
    public void empty() {
        Counter counter = new Counter();
        clock.increaseTime(1000);

        CounterMetric metric = counter.calculateMetric();
        assertEquals(0, metric.totalCount);
        assertEquals(0d, metric.lastRate, 0);
    }
}
