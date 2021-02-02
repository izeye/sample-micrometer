package learningtest.io.micrometer.core.instrument;

import com.google.common.math.Quantiles;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tests for {@link DistributionSummary}.
 *
 * @author Johnny Lim
 */
class DistributionSummaryTests {

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    void distributionSummaryPercentilePrecision(int precision) {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                values.add(i);
            }
        }

        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        DistributionSummary distributionSummary = DistributionSummary.builder("my-distribution-summary")
                .percentilePrecision(precision)
                .publishPercentiles(0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99, 1)
                .register(meterRegistry);

        for (Integer value : values) {
            distributionSummary.record(value);
        }

        HistogramSnapshot snapshot = distributionSummary.takeSnapshot();
        System.out.println("Total: " + snapshot.total());
        System.out.println("Count: " + snapshot.count());
        System.out.println("Mean: " + snapshot.mean());
        System.out.println("Max: " + snapshot.max());

        Map<Integer, Double> micrometerPercentiles = new TreeMap<>();
        for (ValueAtPercentile value : snapshot.percentileValues()) {
            micrometerPercentiles.put(Integer.valueOf((int) (value.percentile() * 100)), value.value());
        }
        System.out.println("Micrometer percentiles: " + micrometerPercentiles);

        Map<Integer, Double> guavaPercentiles = Quantiles.percentiles()
                .indexes(10, 25, 50, 75, 90, 95, 99, 100)
                .compute(values);
        System.out.println("Guava percentiles: " + guavaPercentiles);
    }

}
