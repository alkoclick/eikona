package eikona.ui.templates

import eikona.api.UserSessionPrincipal
import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.p

object StandardElements {

    fun BODY.topBar(user: UserSessionPrincipal) {
        div("ui two item menu") {
            div("ui fluid container") {
                p("ui item") {
                    text("User: ${user.name}")
                }
                a(href = "/logout", classes = "ui item") {
                    text("Logout")
                }
            }
        }
    }
}