package eikona.ui.templates

import io.ktor.server.auth.*
import kotlinx.html.*

interface AuthenticatedPageTemplate : DefaultPageTemplate {
    val user: UserIdPrincipal

    override fun HTML.render() {
        head {
            renderHead()
        }
        body {
            renderTopBar()
            renderBody()
        }
    }

    fun BODY.renderTopBar() {
        div("ui two item menu") {
            div("ui fluid container") {
                p("ui item") {
                    text("User: ${user?.name ?: "Not logged in"}")
                }
                a(href = "/logout", classes = "ui item") {
                    text("Logout")
                }
            }
        }
    }
}