package eikona.api

import eikona.di.DI
import eikona.ui.pages.HomePage
import eikona.ui.pages.ImagePage
import eikona.ui.pages.LoginPage
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

fun Route.uiRoutes() {
    static("assets") {
        staticRootFolder = File("src/main/resources/assets")
        files(".")
    }
    authenticate("auth-session") {
        get("/file/{id?}") {
            call.respondHtml { ImagePage(call).apply { render() } }
        }
    }
}

fun Route.loginRoutes() {
    authenticate("auth-session") {
        get("/") {
            call.respondHtml { HomePage(call.principal()!!).apply { render() } }
        }
    }
    get("/login") {
        call.respondHtml { LoginPage().apply { render() } }
    }
    get("/logout") {
        call.sessions.clear<UserIdPrincipal>()
        DI.sessionStorage.invalidate(call.principal<UserIdPrincipal>()?.name.toString())
        call.respondRedirect("/login")
    }
    authenticate("auth-form") {
        post("/login") {
            val principal = call.principal<UserIdPrincipal>()
            // Set the cookie
            call.sessions.set(principal)
            call.respondRedirect("/")
        }
    }
}