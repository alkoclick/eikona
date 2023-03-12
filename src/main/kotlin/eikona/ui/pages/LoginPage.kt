package eikona.ui.pages

import eikona.ui.templates.DefaultPageTemplate
import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2

class LoginPage : DefaultPageTemplate {

    override fun renderBody(body: BODY) {
        body.div("ui middle aligned center aligned fullpage grid") {
            div("nottoowide column") {
                h2("ui image header") {
                    div(classes = "content") {
                        text("Log in to your Eikona account")
                    }
                }
                div("ui message") {
                    text("New to Eikona? ")
                    a(href = "/authenticate") {
                        text("Create an account")
                    }
                }
            }

        }
    }
}