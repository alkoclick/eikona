package eikona.utils.logger

import eikona.utils.metrics.Metrics
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LoggerMetricsTest {

    @Test
    fun logbackMetrics() {
        val initialWarnCounterValue = Metrics.registry.filteredMetricFamilySamples(setOf("logback_appender_total")).toList().first().samples.find { it.labelValues.contains("warn") }?.value ?: 0.0
        val initialInfoCounterValue = Metrics.registry.filteredMetricFamilySamples(setOf("logback_appender_total")).toList().first().samples.find { it.labelValues.contains("info") }?.value ?: 0.0

        Logger(javaClass).warn("Output for logger metrics testing")
        Logger(javaClass).info("Output for logger metrics testing")
        Logger(javaClass).info("Output for logger metrics testing")
        Logger(javaClass).info("Output for logger metrics testing")
        Logger(javaClass).info("Output for logger metrics testing")
        Logger(javaClass).info("Output for logger metrics testing")

        assertEquals(1, Metrics.registry.filteredMetricFamilySamples(setOf("logback_appender_total")).toList().size)
        assertEquals(
            initialWarnCounterValue + 1.0,
            Metrics.registry.filteredMetricFamilySamples(setOf("logback_appender_total")).toList().first().samples.find { it.labelValues.contains("warn") }!!.value
        )
        assertEquals(
            initialInfoCounterValue + 5.0,
            Metrics.registry.filteredMetricFamilySamples(setOf("logback_appender_total")).toList().first().samples.find { it.labelValues.contains("info") }!!.value
        )
    }
}