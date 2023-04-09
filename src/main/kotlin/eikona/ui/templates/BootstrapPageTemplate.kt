package eikona.ui.templates

import kotlinx.html.*

interface BootstrapPageTemplate : UIPage {

    val additionalCssHref: String?

    override fun render(html: HTML) {
        html.head {
            renderHead(this)
        }
        html.body {
            renderBody(this)
        }
    }

    fun renderHead(head: HEAD) {
        head.apply {
            title = "Eikona"
            meta(charset = "utf-8")
            meta(name = "viewport", content = "width=device-width, initial-scale=1.0, maximum-scale=1.0")
            meta(name = "theme-color", content = "#d72ef4")
            meta {
                this.httpEquiv = "X-UA-Compatible"
                this.content = "IE=edge,chrome=1"
            }
            link(rel = "stylesheet", type = "text/css", href = "/assets/css/custom.css")
            additionalCssHref?.let {
                link(rel = "stylesheet", type = "text/css", href = it)
            }
        }
    }

    fun renderBody(body: BODY)
}