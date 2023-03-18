package eikona.utils.logger

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class LoggerTest {

    @Test
    fun create() {
        val logger = Logger(javaClass)

        logger.debug("Test message")
    }

    @Test
    fun duplicateLoggers() {
        assertDoesNotThrow {
            Logger(javaClass)
            Logger(javaClass)
        }
    }

    @Test
    fun nonThrows() {
        assertDoesNotThrow {
            val logger = Logger(javaClass)
            logger.debug("This is a test message")
            logger.info("This is a test message")
            logger.warn("This is a test message")
            logger.panic("This is a test message")
        }
    }
}