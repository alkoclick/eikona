package eikona.api

import eikona.di.DI
import eikona.ui.pages.*
import eikona.utils.logger.Logger
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

object UIRoutes {
    internal val logger = Logger(javaClass)
}

fun Route.uiRoutes() {
    static("assets") {
        staticBasePackage = "assets"
        resources(".")
    }
    authenticate("auth-session") {
        get("/") {
            call.respondHtml { HomePage(call.principal()!!).render(this) }
        }
        get("/file/{id?}") {
            call.respondHtml { ImagePage(call).render(this) }
        }
        get("/collection/{id?}") {
            call.respondHtml { CollectionPage(call).render(this) }
        }
        get("/upload") {
            call.respondHtml { UploadPage(call).render(this) }
        }
    }
}

fun Route.loginRoutes() {

    // TODO Set a cookie with state?
    get("/login") {
        call.respondHtml { LoginPage().render(this) }
    }

    get("/logout") {
        call.sessions.clear<UserSessionPrincipal>()
        DI.sessionStorage.invalidate(call.principal<UserSessionPrincipal>()?.name.toString())
        call.respondRedirect("/login")
    }
}
