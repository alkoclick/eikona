package eikona.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AppInfoRoutesSmokeTest {

    @Test
    fun testHealth() = testApplication {
        val response = client.get("/api/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Ok!", response.bodyAsText())
    }
}