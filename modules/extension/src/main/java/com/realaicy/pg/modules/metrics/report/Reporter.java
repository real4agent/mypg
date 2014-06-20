package com.realaicy.pg.modules.metrics.report;

import com.realaicy.pg.modules.metrics.CounterMetric;
import com.realaicy.pg.modules.metrics.ExecutionMetric;
import com.realaicy.pg.modules.metrics.HistogramMetric;

import java.util.Map;

public interface Reporter {
    void report(Map<String, CounterMetric> counters, Map<String, HistogramMetric> histograms,
                Map<String, ExecutionMetric> executions);
}
