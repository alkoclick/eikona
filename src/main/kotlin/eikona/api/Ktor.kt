package eikona.api

import appInfoRoutes
import eikona.di.DI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlin.collections.set


@Suppress("unused")
fun Application.config() {
    install(Sessions) {
        cookie<UserSessionPrincipal>(
            // We set a cookie by this name upon login.
            "eikona",
            storage = DI.sessionStorage
        ) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 7L * 24 * 3600 // 7 days
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies#SameSite_cookies
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Authentication) {
        session("auth-session") {
            challenge {
                call.respondRedirect("/login")
            }
            validate { session: UserSessionPrincipal -> session }
        }
    }
    routing {
        appInfoRoutes()
        blobRouting()
        uiRoutes()
        loginRoutes()
    }
}

data class UserSessionPrincipal(val id: String, val name: String) : Principal

