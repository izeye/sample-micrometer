package learningtest.io.micrometer.core.instrument.binder.okhttp3;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.lanwen.wiremock.ext.WiremockResolver;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OkHttpMetricsEventListener}.
 *
 * @author Johnny Lim
 */
@ExtendWith(WiremockResolver.class)
class OkHttpMetricsEventListenerTests {

	private final SimpleMeterRegistry registry = new SimpleMeterRegistry();

	@Test
	void test(@WiremockResolver.Wiremock WireMockServer server) throws IOException {
		String name = "okhttp.requests";

		OkHttpMetricsEventListener metricsListener = OkHttpMetricsEventListener
				.builder(this.registry, name).build();
		OkHttpClient client = new OkHttpClient.Builder().eventListener(metricsListener)
				.build();

		Request request = new Request.Builder().url(server.baseUrl()).build();
		client.newCall(request).execute().close();

		assertThat(this.registry.get(name).timer().count()).isEqualTo(1L);
	}

}
