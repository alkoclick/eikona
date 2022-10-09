package eikona.ui.pages

import eikona.ui.templates.AuthenticatedPageTemplate
import io.ktor.server.auth.*
import kotlinx.html.BODY
import kotlinx.html.div
import kotlinx.html.h2

class HomePage(override val user: UserIdPrincipal) : AuthenticatedPageTemplate {

    override fun renderBody(body: BODY) {
        body.div("ui middle aligned center aligned fullpage grid") {
            this.div("column") {
                this.h2("ui header") {
                    this.div(classes = "content") {
                        this.text("Welcome to Eikona!")
                    }
                }
            }
        }
    }
}