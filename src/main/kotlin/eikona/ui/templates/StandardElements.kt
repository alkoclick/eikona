package eikona.ui.templates

import eikona.api.UserSessionPrincipal
import kotlinx.html.HEADER
import kotlinx.html.a
import kotlinx.html.div

object StandardElements {

    fun HEADER.topBar(user: UserSessionPrincipal) {
        div(classes = "navbar") {
            div(classes = "container-fluid") {
                a(href = "/", classes = "h3 text-center link-primary eikona-title") {
                    text("Eikona")
                }
                a(href = "/logout", classes = "navbar-text") {
                    text("Logout")
                }
            }
        }
    }
}