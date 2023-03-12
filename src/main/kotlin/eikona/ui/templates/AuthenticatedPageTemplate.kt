package eikona.ui.templates

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.StandardElements.topBar
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head

interface AuthenticatedPageTemplate : DefaultPageTemplate {
    val user: UserSessionPrincipal

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