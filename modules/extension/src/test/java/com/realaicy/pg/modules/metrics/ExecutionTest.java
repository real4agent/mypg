package com.realaicy.pg.modules.metrics;

import com.realaicy.pg.modules.metrics.utils.Clock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExecutionTest {

    @Test
    public void normal() {
        Clock.MockClock clock = new Clock.MockClock();
        Execution.clock = clock;
        Counter.clock = clock;
        Execution execution = new Execution(90d);

        Execution.ExecutionTimer timer = execution.start();
        clock.increaseTime(200);
        timer.stop();

        Execution.ExecutionTimer timer2 = execution.start();
        clock.increaseTime(500);
        timer2.stop();

        ExecutionMetric metric = execution.calculateMetric();

        assertEquals(2, metric.counterMetric.totalCount);
        assertEquals(2, metric.counterMetric.lastRate, 0);

        assertEquals(200, metric.histogramMetric.min);
        assertEquals(350, metric.histogramMetric.mean, 0);
        assertEquals(500, metric.histogramMetric.pcts.get(90d), 0);
    }
}
