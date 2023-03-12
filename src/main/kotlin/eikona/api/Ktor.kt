package eikona.api

import appInfoRoutes
import com.auth0.jwk.JwkProviderBuilder
import eikona.Config
import eikona.di.DI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.concurrent.TimeUnit
import kotlin.collections.set


fun Application.config() {
    install(Sessions) {
        cookie<UserIdPrincipal>(
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
            validate { session: UserIdPrincipal -> session }
        }
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                UserIdPrincipal(credentials.name)
            }
        }
        val jwkProvider = JwkProviderBuilder(Config.auth.issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
        jwt("auth0") {
            verifier(jwkProvider, Config.auth.issuer)
            validate { credential -> validateCreds(credential) }
        }
    }
    routing {
        appInfoRoutes()
        blobRouting()
        uiRoutes()
        loginRoutes()
    }
}

fun validateCreds(credential: JWTCredential): JWTPrincipal? =
    credential.payload
        .takeIf { it.audience.contains(Config.auth.audience) }
        ?.let { JWTPrincipal(it) }
