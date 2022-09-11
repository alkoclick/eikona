package eikona.api

import appInfoRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.config() {
    routing {
        blobRouting()
        appInfoRoutes()
    }
}

