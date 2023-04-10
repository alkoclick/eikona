package eikona.ui.templates

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.StandardElements.topBar
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.header

interface AuthenticatedPageTemplate : BootstrapPageTemplate {
    val user: UserSessionPrincipal
    override val additionalCssHref: String?
        get() = null

    override fun render(html: HTML) {
        html.head {
            renderHead(this)
        }
        html.body {
            header { topBar(user) }
            renderBody(this)
        }
    }


}