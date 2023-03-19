package eikona.api

import arrow.core.Either
import eikona.Config
import eikona.utils.http.HTTPResponse
import eikona.utils.http.HTTPService
import eikona.utils.json.toModel
import eikona.utils.jwt.JWTService
import eikona.utils.logger.Logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

object OIDCRoutes {
    internal val logger = Logger(javaClass)
}

fun Route.oidcRoutes() {
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

    get("/callback") {
        val response = HTTPService.post(Config.auth.endpoints.token, ParametersBuilder().apply {
            append("grant_type", "authorization_code")
            append("code", call.request.queryParameters["code"]!!)
            append("client_id", Config.auth.client_id)
            append("client_secret", Config.auth.secret)
            append("redirect_uri", "http://localhost:8080/upload")
        }.build().formUrlEncode(), "Content-Type" to "application/x-www-form-urlencoded")

        when (response) {
            is HTTPResponse.Error -> OIDCRoutes.logger.panic(response.message)
            is HTTPResponse.Received -> {
                val oidcResponse = response.body?.toModel(OIDCResponse::class)

                when (val jwt = JWTService.decode(oidcResponse?.id_token.orEmpty())) {
                    is Either.Left -> OIDCRoutes.logger.panic("JWT decoding or verification failed: ", jwt.value)
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

data class OIDCResponse(
    val access_token: String,
    val id_token: String,
    val scope: String = "",
    val expires_in: Int,
)
