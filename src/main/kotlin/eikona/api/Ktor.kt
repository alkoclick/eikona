package eikona.api

import appInfoRoutes
import eikona.di.DI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
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
//        oauth {
//            urlProvider = { "" }
//            providerLookup = {
//                OAuthServerSettings.OAuth2ServerSettings(
//                    name = "TestAuth0",
//                    authorizeUrl = "",
//                    requestMethod = HttpMethod.Post,
//                    clientId = "1vNXgpgZa25PGWt33JiYKG3OMb2kNrPL",
//                    clientSecret = "gMHqaLxD-CzVK9uDr0Gr1Ok-19gRq1SqPcbJA2fIeOaIMyVHeCcMbJr1cciIg4Ez",
//                )
//            }
//            client = httpClient
//        }
    }
    routing {
        appInfoRoutes()
        blobRouting()
        uiRoutes()
        loginRoutes()
    }
}


