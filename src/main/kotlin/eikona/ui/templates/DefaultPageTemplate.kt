package eikona.ui.templates

import kotlinx.html.*

interface DefaultPageTemplate : UIPage {

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
            meta {
                this.httpEquiv = "X-UA-Compatible"
                this.content = "IE=edge,chrome=1"
            }
            link(rel = "stylesheet",
                type = "text/css",
                href = "https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css")
            script(src = "https://code.jquery.com/jquery-3.1.1.min.js") {
                this.integrity = "sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
                this.attributes["crossorigin"] = "anonymous"
            }
            script(src = "https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js") {}
            link(rel = "stylesheet", type = "text/css", href = "/assets/css/main.css")
        }
    }

    fun renderBody(body: BODY)
}