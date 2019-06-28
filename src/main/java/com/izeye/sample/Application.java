package com.izeye.sample;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;

/**
 * Application.
 *
 * @author Johnny Lim
 */
public class Application {

	public static void main(String[] args) {
		testInfluxMeterRegistry();
	}

	private static void testInfluxMeterRegistry() {
		long startTimeMillis = System.currentTimeMillis();

		InfluxConfig influxConfig = new InfluxConfig() {

			@Override
			public String get(String key) {
				return null;
			}

			@Override
			public Duration step() {
				return Duration.ofSeconds(10);
			}

		};
		InfluxMeterRegistry influxMeterRegistry = new InfluxMeterRegistry(influxConfig,
				Clock.SYSTEM);

		Counter counter = influxMeterRegistry.counter("my.counter");
		counter.increment();
		System.out.println(counter.count());

		sleep(10);
		System.out.println(counter.count());

		sleep(10);
		System.out.println(counter.count());

		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		System.out.println("Elapsed time (ms): " + elapsedTimeMillis);
	}

	private static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
