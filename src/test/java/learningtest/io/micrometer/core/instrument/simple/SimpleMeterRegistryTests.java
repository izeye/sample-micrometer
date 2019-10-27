package learningtest.io.micrometer.core.instrument.simple;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SimpleMeterRegistry}.
 *
 * @author Johnny Lim
 */
class SimpleMeterRegistryTests {

	private final MeterRegistry registry = new SimpleMeterRegistry();

	@Test
	void test() {
		Counter counter = this.registry.counter("test");
		counter.increment();
		assertThat(counter.count()).isEqualTo(1);
	}

}
