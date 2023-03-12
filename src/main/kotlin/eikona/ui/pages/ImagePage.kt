package eikona.ui.pages

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.AuthenticatedPageTemplate
import io.ktor.server.application.*
import io.ktor.server.auth.*
import kotlinx.html.*

class ImagePage(call: ApplicationCall) : AuthenticatedPageTemplate {
    val imgUuid = call.parameters["id"]
    override val user: UserSessionPrincipal = call.principal()!!

    override fun renderHead(head: HEAD) {
        super.renderHead(head)
        head.apply {
            script(src = "https://unpkg.com/ml5@latest/dist/ml5.min.js", block = {})
            script(src = "/assets/js/faceRecognition.js", block = {})
        }
    }

    override fun renderBody(body: BODY) {
        body.apply {
            div("ui middle aligned center aligned fullpage grid") {
                div("column") {
                    // Populated with face detection
                    h2("ui header")
                    img(src = "/api/blob/$imgUuid", classes = "img-fluid frame img")
                }
            }
        }
    }

}