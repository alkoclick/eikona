package eikona.utils.logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import eikona.Config

/**
 * Custom Logback [Filter] for Eikona.
 *
 * While we could technically do our job with stuff such as the [ch.qos.logback.classic.filter.ThresholdFilter] and
 * a bunch of XML, that is simply too horrible to read/write. Configuring that was deemed too wasteful/closed,
 * so we just filter here, with full control, and in a way everyone can read and edit
 */
class LoggerFilter : Filter<ILoggingEvent>() {

    /**
     * Whether an incoming log event should be logged or not
     */
    override fun decide(event: ILoggingEvent?): FilterReply {
        if (event == null) return FilterReply.DENY

        for (entry in Config.logger.filters)
            if (event.loggerName?.startsWith(entry.key) == true)
                return acceptEqOrHigher(event, Level.toLevel(entry.value))

        return acceptEqOrHigher(event, Level.toLevel(Config.logger.base_level))
    }

    private fun acceptEqOrHigher(event: ILoggingEvent, minLevel: Level) =
        when (event.level?.isGreaterOrEqual(minLevel)) {
            true -> FilterReply.NEUTRAL
            else -> FilterReply.DENY
        }
}