package eikona.ui.pages

import eikona.ui.templates.AuthenticatedPageTemplate
import io.ktor.server.auth.*
import kotlinx.html.BODY
import kotlinx.html.div
import kotlinx.html.h2

class HomePage(override val user: UserIdPrincipal) : AuthenticatedPageTemplate {

    override fun BODY.renderBody() {
        div("ui middle aligned center aligned fullpage grid") {
            div("column") {
                h2("ui header") {
                    div(classes = "content") {
                        text("Welcome to Eikona!")
                    }
                }
            }
        }
    }
}