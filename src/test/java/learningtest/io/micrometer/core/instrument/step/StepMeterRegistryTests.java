package learningtest.io.micrometer.core.instrument.step;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link StepMeterRegistry}.
 *
 * @author Johnny Lim
 */
public class StepMeterRegistryTests {

	private static final int STEP = 5;

	private final StepRegistryConfig config = new StepRegistryConfig() {
		@Override
		public String prefix() {
			return null;
		}

		@Override
		public String get(String key) {
			return null;
		}

		@Override
		public Duration step() {
			return Duration.ofSeconds(STEP);
		}
	};

	private final MeterRegistry registry = new StepMeterRegistry(config, Clock.SYSTEM) {
		@Override
		protected void publish() {
		}

		@Override
		protected TimeUnit getBaseTimeUnit() {
			return TimeUnit.MILLISECONDS;
		}
	};

	@Test
	public void test() throws InterruptedException {
		Counter counter = this.registry.counter("test");
		counter.increment();
		counter.increment();
		assertThat(counter.count()).isEqualTo(0);

		TimeUnit.SECONDS.sleep(STEP);
		assertThat(counter.count()).isEqualTo(2);

		TimeUnit.SECONDS.sleep(STEP);
		assertThat(counter.count()).isEqualTo(0);
	}

}
