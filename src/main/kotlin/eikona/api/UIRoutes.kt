package eikona.api

import com.auth0.jwt.JWT
import com.beust.klaxon.Klaxon
import eikona.Config
import eikona.api.models.OAuthIdTokenPayload
import eikona.api.models.OIDCResponse
import eikona.di.DI
import eikona.ui.pages.HomePage
import eikona.ui.pages.ImagePage
import eikona.ui.pages.LoginPage
import eikona.ui.pages.UploadPage
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File
import java.util.*

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
        client.post(Config.auth.endpoints.token) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(ParametersBuilder().apply {
                append("grant_type", "authorization_code")
                append("code", call.request.queryParameters["code"]!!)
                append("client_id", Config.auth.client_id)
                append("client_secret", Config.auth.secret)
                append("redirect_uri", "http://localhost:8080/upload")
            }.build().formUrlEncode())
        }.let {
            val oidcResponse: OIDCResponse = Klaxon().parse(it.bodyAsText())!!

            // Convert to JWT
            val jwt = JWT.decode(oidcResponse.id_token)
            println(jwt.payload)

            // Verify JWT
            // TODO

            // Unwrap JWT
            val userInfo: OAuthIdTokenPayload = Klaxon().parse(String(Base64.getDecoder().decode(jwt.payload)))!!

            call.sessions.set(UserSessionPrincipal(userInfo.sub, userInfo.name))
        }
        call.respondRedirect("http://localhost:8080/upload")
    }
}

val client by lazy { HttpClient() }