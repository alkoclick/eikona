package eikona.utils.http

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HTTPServiceTest {

    @Test
    fun schemeCleanup() {
        with(HTTPService) {
            assertEquals("localhost", removeIdsFromPathForMetrics("http://localhost"))
            assertEquals("localhost", removeIdsFromPathForMetrics("https://localhost"))
            assertEquals("google.com", removeIdsFromPathForMetrics("https://google.com"))
        }
    }

    @Test
    fun pathCleanup() {
        with(HTTPService) {
            assertEquals("localhost", removeIdsFromPathForMetrics("http://localhost"))
            assertEquals("localhost", removeIdsFromPathForMetrics("https://localhost"))
            assertEquals("google.com", removeIdsFromPathForMetrics("https://google.com"))
            assertEquals("google.com/abc/xyz", removeIdsFromPathForMetrics("https://google.com/abc/xyz"))
            assertEquals("google.com/abc/xyz", removeIdsFromPathForMetrics("https://google.com/abc/xyz?"))
            assertEquals("google.com/abc/xyz", removeIdsFromPathForMetrics("https://google.com/abc/xyz?abc=123"))
            assertEquals("google.com/abc/{int}/xyz", removeIdsFromPathForMetrics("https://google.com/abc/123/xyz"))
            assertEquals("google.com/abc/{int}/{int}/xyz", removeIdsFromPathForMetrics("https://google.com/abc/123/456/xyz"))
            assertEquals("google.com/abc/xyz/{int}", removeIdsFromPathForMetrics("https://google.com/abc/xyz/123"))
            assertEquals("google.com/abc/xyz/{int}", removeIdsFromPathForMetrics("https://google.com/abc/xyz/123?abc=123"))
        }
    }
}