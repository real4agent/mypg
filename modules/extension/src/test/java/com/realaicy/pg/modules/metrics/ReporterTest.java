package com.realaicy.pg.modules.metrics;

import com.realaicy.pg.modules.metrics.report.*;
import com.realaicy.pg.modules.metrics.utils.Clock;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ReporterTest {

    @Test
    public void consoleReporter() {
        runReport(new ConsoleReporter());
    }

    @Test
    public void slf4jReporter() {
        runReport(new Slf4jReporter());
    }

    @Test
    @Ignore("manual test")
    public void graphiteReporter() {
        runReport(new GraphiteReporter(new InetSocketAddress("localhost", 2003)));
    }

    @Test
    public void schedulerStartStop() throws InterruptedException {
        ReportScheduler scheduler = new ReportScheduler(new MetricRegistry(), new ConsoleReporter());
        scheduler.start(1, TimeUnit.SECONDS);
        Thread.sleep(2000);
        scheduler.stop();
    }

    private void runReport(Reporter reporter) {
        MetricRegistry metricRegistry = new MetricRegistry();
        Clock.MockClock clock = new Clock.MockClock();
        Counter.clock = clock;
        Execution.clock = clock;

        // counter
        Counter counter = metricRegistry.counter(MetricRegistry.name("UserService", "getUser.counter"));
        counter.inc(4);
        Counter counter2 = metricRegistry.counter(MetricRegistry.name("UserService", "setUser.counter"));
        counter2.inc(6);
        clock.increaseTime(1000);

        // histogram
        Histogram histogram = metricRegistry.histogram(MetricRegistry.name("UserService", "getUser.latency"));
        for (int i = 1; i <= 100; i++) {
            histogram.update(i);
        }
        Histogram histogram2 = metricRegistry.histogram(MetricRegistry.name("UserService", "setUser.latency"));
        for (int i = 1; i <= 100; i++) {
            histogram2.update(i * 2);
        }

        // execution
        Execution execution = metricRegistry.execution(MetricRegistry.name("UserService", "getUser.timer"));
        for (int i = 1; i <= 10; i++) {
            Execution.ExecutionTimer timer = execution.start();
            clock.increaseTime(25);
            timer.stop();
        }
        Execution execution2 = metricRegistry.execution(MetricRegistry.name("UserService", "setUser.timer"));
        for (int i = 1; i <= 10; i++) {
            Execution.ExecutionTimer timer = execution2.start();
            clock.increaseTime(75);
            timer.stop();
        }

        // totally 2 seconds past
        ReportScheduler scheduler = new ReportScheduler(metricRegistry, reporter);
        scheduler.report();
    }
}
