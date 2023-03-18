package eikona.utils.metrics

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import io.prometheus.client.Summary

/**
 * A registry for all our metrics, currently collected via Prometheus.
 * We use a centralized approach to avoid losing track of the various
 * metrics we export and to apply a unified standard.
 *
 * Naming things is hard, but please stick to `snake_case`, the Prometheus
 * [standard naming convention](https://prometheus.io/docs/practices/naming/)
 *
 * For additional readup, please visit:
 * - [Prometheus quickstart](https://prometheus.io/docs/introduction/overview/)
 * - [Prometheus java metrics readme](https://github.com/prometheus/client_java)
 */
object Metrics {

    val registry: CollectorRegistry = CollectorRegistry.defaultRegistry

    /**
     * A shared namespace for all our manually defined metrics
     */
    private const val namespace = "eikona"

    /**
     * Register a counter. A counter is a metric whose value only goes up.
     * If you want something whose value also goes down, use a [io.prometheus.client.Gauge].
     *
     * Typically we plot the absolute value and rate of counter change.
     *
     * @param name the name of the metric
     * @param help the description of the metric. WILL CRASH IF YOU DON'T SET THIS
     *
     * @see [https://github.com/prometheus/client_java#counter]
     */
    fun counter(name: String, help: String, vararg labelNames: String): Counter =
        Counter
            .build("${namespace}_$name", help)
            .labelNames(*labelNames)
            .register(registry)

    /**
     * Register a gauge. A counter is a metric whose value can go both up and down. A gauge offers
     * less support for rate of change than a counter, as it's not monotonic.
     *
     * @param name the name of the metric
     * @param help the description of the metric. WILL CRASH IF YOU DON'T SET THIS
     *
     * @see [https://github.com/prometheus/client_java#gauge]
     */
    fun gauge(name: String, help: String, vararg labelNames: String): Gauge =
        Gauge
            .build("${namespace}_$name", help)
            .labelNames(*labelNames)
            .register(registry)

    /**
     * Register a summary. A summary is used to track results into quantiles
     * and offer useful APIs for timing.
     *
     * @param name the name of the metric
     * @param help the description of the metric. WILL CRASH IF YOU DON'T SET THIS
     * @param labelNames additional labels
     *
     * @see [https://github.com/prometheus/client_java#summary]
     */
    fun summary(name: String, help: String, vararg labelNames: String): Summary =
        Summary
            .build("${namespace}_$name", help)
            .labelNames(*labelNames)
            .quantile(0.5, 0.05)   // Add 50th percentile (= median) with 5% tolerated error
            .quantile(0.9, 0.01)   // Add 90th percentile with 1% tolerated error
            .quantile(0.99, 0.001) // Add 99th percentile with 0.1% tolerated error
            .register(registry)

    object HTTPRequests {
        val httpRequests = summary("http_client_total_requests", "The number of requests executed by the HTTP client", "path", "method", "status_code")
    }
}