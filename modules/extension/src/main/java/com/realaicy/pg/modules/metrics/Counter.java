package com.realaicy.pg.modules.metrics;

import com.realaicy.pg.modules.metrics.utils.Clock;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {
	public static Clock clock = Clock.DEFAULT;

	private AtomicLong counter = new AtomicLong(0);

	private long totalCount = 0L;
	private long lastReportTime;

	public Counter() {
		lastReportTime = clock.getCurrentTime();
	}

	public void inc() {
		counter.incrementAndGet();
	}

	public void inc(long n) {
		counter.addAndGet(n);
	}

	public void dec() {
		counter.decrementAndGet();
	}

	public void dec(long n) {
		counter.addAndGet(-n);
	}

    public CounterMetric calculateMetric() {
        long lastCount = counter.getAndSet(0);
        long currentTime = clock.getCurrentTime();

        CounterMetric metric = new CounterMetric();

        totalCount += lastCount;
        metric.lastCount = lastCount;
        metric.totalCount = totalCount;

        long elapsed = currentTime - lastReportTime;
        if (elapsed > 0) {
            metric.lastRate = (lastCount * 1000) / elapsed;
        }

        lastReportTime = currentTime;
        return metric;
    }
}
