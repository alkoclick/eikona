package eikona.ui.pages

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.AuthenticatedPageTemplate
import kotlinx.html.BODY
import kotlinx.html.div
import kotlinx.html.h2

class HomePage(override val user: UserSessionPrincipal) : AuthenticatedPageTemplate {

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