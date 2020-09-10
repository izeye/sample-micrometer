package learningtest.io.micrometer.core.instrument.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MeterFilter}.
 *
 * @author Johnny Lim
 */
class MeterFilterTests {

	@Test
	void map() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();

		registry.config().meterFilter(new MeterFilter() {
			@Override
			public Meter.Id map(Meter.Id id) {
				return id.withName("renamed");
			}

			@Override
			public MeterFilterReply accept(Meter.Id id) {
				assertThat(id.getName()).isEqualTo("renamed");
				return MeterFilter.super.accept(id);
			}
		})
		.meterFilter(new MeterFilter() {
			@Override
			public MeterFilterReply accept(Meter.Id id) {
				assertThat(id.getName()).isEqualTo("renamed");
				return MeterFilter.super.accept(id);
			}
		});

		Counter counter = registry.counter("my.counter", Tags.of("tag1", "value1"));
		assertThat(counter.getId().getName()).isEqualTo("renamed");
	}
}
