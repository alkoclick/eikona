package eikona.ui.templates

import eikona.ui.templates.StandardElements.topBar
import io.ktor.server.auth.*
import kotlinx.html.*

interface AuthenticatedPageTemplate : DefaultPageTemplate {
    val user: UserIdPrincipal

    override fun render(html: HTML) {
        html.head {
            renderHead(this)
        }
        html.body {
            topBar(user)
            renderBody(this)
        }
    }


}