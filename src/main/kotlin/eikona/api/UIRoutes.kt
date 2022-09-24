package eikona.api

import eikona.ui.ImagePage
import eikona.ui.LoginPage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.uiRoutes() {
    authenticate("auth-session") {
        get("/file/{id?}") {
            call.respondHtml { ImagePage(call).apply { render() } }
        }
    }
}

fun Route.loginRoutes() {
    get("/login") {
        call.respondHtml { LoginPage().apply { render() } }
    }
    authenticate("auth-form") {
        post("/login") {
            val principal = call.principal<UserIdPrincipal>()
            // Set the cookie
            call.sessions.set(principal)
            call.respond(status = HttpStatusCode.OK, "Ok!")
        }
    }
}