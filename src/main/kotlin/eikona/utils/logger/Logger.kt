package eikona.utils.logger

import org.slf4j.LoggerFactory
import org.slf4j.Logger as Slf4jLogger

/**
 * Log any eikona message, now with 110% more ship analogies!
 *
 * Initialize like:
 * ```
 * val logger = Logger(javaClass)
 * ```
 *
 * Always use over any other logger! This logger allows us to
 * abstract away the underlying implementation and easily change it
 *
 * If in doubt about level, use info.
 */
open class Logger(clazz: Class<*>) {

    private val internalLogger: Slf4jLogger = LoggerFactory.getLogger(clazz)

    /**
     * Expose some of the code's inner working details for troubleshooting
     *
     * @param message the message to log
     * @implSpec write to default handler
     */
    open fun debug(message: String) {
        internalLogger.debug(message)
    }

    /**
     * Describe interesting or important landmarks the code is observing
     *
     * @param message the message to log
     * @implSpec write to default handler
     */
    open fun info(message: String) {
        internalLogger.info(message)
    }

    /**
     * Ahoy! The code is navigating potentially treacherous waters
     *
     * @param message the message to log
     * @implSpec report this to an external system
     */
    open fun warn(message: String) {
        internalLogger.warn(message)
    }

    /**
     * Mayday! The code is about to crash ashore
     *
     * @param message the message to log
     * @implSpec report this to an external system and ensure delivery
     */
    open fun panic(message: String, exception: Throwable? = null) {
        when (exception) {
            null -> internalLogger.error(message)
            else -> internalLogger.error(message, exception)
        }
    }
}