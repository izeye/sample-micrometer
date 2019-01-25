package com.izeye.sample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Application.
 *
 * @author Johnny Lim
 */
public class Application {

	public static void main(String[] args) {
		testPrometheusMeterRegistry();
	}

	private static void testPrometheusMeterRegistry() {
		PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

		System.out.println("### After creating PrometheusMeterRegistry, scrape():");
		System.out.println(prometheusMeterRegistry.scrape());

		Counter counter = prometheusMeterRegistry.counter("my.counter");
		System.out.println("### After creating a counter, scrape():");
		System.out.println(prometheusMeterRegistry.scrape());

		counter.increment();
		System.out.println("### After incrementing the counter, scrape():");
		System.out.println(prometheusMeterRegistry.scrape());

		try {
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
			httpServer.createContext("/prometheus", httpExchange -> {
				String response = prometheusMeterRegistry.scrape();
				httpExchange.sendResponseHeaders(200, response.getBytes().length);
				try (OutputStream os = httpExchange.getResponseBody()) {
					os.write(response.getBytes());
				}
			});
			new Thread(httpServer::start).start();
			System.out.println("### Listening on http://localhost:8080/prometheus");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
