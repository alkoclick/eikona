package eikona.api

import arrow.core.Either
import eikona.Config
import eikona.api.models.OIDCResponse
import eikona.di.DI
import eikona.ui.pages.HomePage
import eikona.ui.pages.ImagePage
import eikona.ui.pages.LoginPage
import eikona.ui.pages.UploadPage
import eikona.utils.http.HTTPResponse
import eikona.utils.http.HTTPService
import eikona.utils.json.toModel
import eikona.utils.jwt.JWTService
import eikona.utils.logger.Logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

object UIRoutes {
    internal val logger = Logger(javaClass)
}

fun Route.uiRoutes() {
    static("assets") {
        staticRootFolder = File("src/main/resources/assets")
        files(".")
    }
    authenticate("auth-session") {
        get("/file/{id?}") {
            call.respondHtml { ImagePage(call).render(this) }
        }
        get("/upload") {
            call.respondHtml { UploadPage(call).render(this) }
        }
    }

    authenticate("auth-session") {
        get("/") {
            call.respondHtml {
                HomePage(call.principal()!!).render(this)
            }
        }
    }
}

fun Route.loginRoutes() {

    // TODO Set a cookie with state?
    get("/login") {
        call.respondHtml { LoginPage().render(this) }
    }

    get("/authenticate") {
        val authParams = ParametersBuilder().apply {
            append("response_type", "code")
            append("client_id", Config.auth.client_id)
            append("redirect_uri", "http://localhost:8080/callback")
            append("prompt", "login")
            append("scope", "openid profile")
            append("state", "abcd")
        }.build().formUrlEncode()
        call.respondRedirect("${Config.auth.endpoints.authorize}?${authParams}")
    }

    get("/logout") {
        call.sessions.clear<UserSessionPrincipal>()
        DI.sessionStorage.invalidate(call.principal<UserSessionPrincipal>()?.name.toString())
        call.respondRedirect("/login")
    }

    get("/callback") {
        val response = HTTPService.post(Config.auth.endpoints.token, ParametersBuilder().apply {
            append("grant_type", "authorization_code")
            append("code", call.request.queryParameters["code"]!!)
            append("client_id", Config.auth.client_id)
            append("client_secret", Config.auth.secret)
            append("redirect_uri", "http://localhost:8080/upload")
        }.build().formUrlEncode(), "Content-Type" to "application/x-www-form-urlencoded")

        when (response) {
            is HTTPResponse.Error -> UIRoutes.logger.panic(response.message)
            is HTTPResponse.Received -> {
                val oidcResponse = response.body?.toModel(OIDCResponse::class)

                when (val jwt = JWTService.decode(oidcResponse?.id_token.orEmpty())) {
                    is Either.Left -> UIRoutes.logger.panic("JWT decoding or verification failed: ", jwt.value)
                    is Either.Right -> {
                        // TODO Should use the name
                        call.sessions.set(UserSessionPrincipal(jwt.value.subject, jwt.value.token))
                        call.respondRedirect("http://localhost:8080/upload")
                    }
                }
            }
        }
    }
}
