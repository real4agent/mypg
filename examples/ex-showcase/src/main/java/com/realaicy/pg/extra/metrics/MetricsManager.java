package com.realaicy.pg.extra.metrics;

import com.realaicy.pg.modules.metrics.MetricRegistry;
import com.realaicy.pg.modules.metrics.report.ConsoleReporter;
import com.realaicy.pg.modules.metrics.report.GraphiteReporter;
import com.realaicy.pg.modules.metrics.report.ReportScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class MetricsManager {

    private ReportScheduler scheduler;

    private boolean graphiteEnabled = false;

    @PostConstruct
    public void start() {
        ConsoleReporter consoleReporter = new ConsoleReporter();
        scheduler = new ReportScheduler(MetricRegistry.INSTANCE, consoleReporter);

        if (graphiteEnabled) {
            GraphiteReporter graphiteReporter = new GraphiteReporter(new InetSocketAddress("localhost", 2003));
            scheduler.addReporter(graphiteReporter);
        }

        scheduler.start(10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        scheduler.stop();
    }

    public void setGraphiteEnabled(boolean graphiteEnabled) {
        this.graphiteEnabled = graphiteEnabled;
    }
}
