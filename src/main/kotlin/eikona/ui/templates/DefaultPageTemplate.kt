package eikona.ui.templates

import kotlinx.html.*

interface DefaultPageTemplate : UIPage {

    override fun HTML.render() {
        head {
            renderHead()
        }
        body {
            renderBody()
        }
    }

    fun HEAD.renderHead() {
        title = "Eikona"
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0, maximum-scale=1.0")
        meta {
            httpEquiv = "X-UA-Compatible"
            content = "IE=edge,chrome=1"
        }
        link(rel = "stylesheet", type = "text/css", href = "https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css")
        script(src = "https://code.jquery.com/jquery-3.1.1.min.js") {
            integrity = "sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
            attributes["crossorigin"] = "anonymous"
        }
        script(src = "https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js", block = {})
        link(rel = "stylesheet", type = "text/css", href = "/assets/css/main.css")
    }

    fun BODY.renderBody()
}