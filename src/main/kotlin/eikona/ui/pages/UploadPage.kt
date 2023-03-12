package eikona.ui.pages

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.AuthenticatedPageTemplate
import io.ktor.server.application.*
import io.ktor.server.auth.*
import kotlinx.html.*

class UploadPage(call: ApplicationCall) : AuthenticatedPageTemplate {
    override val user: UserSessionPrincipal = call.principal()!!

    override fun renderHead(head: HEAD) {
        super.renderHead(head)
        head.apply {
            script(src = "/assets/js/upload.js", block = {})
        }
    }

    override fun renderBody(body: BODY) {
        body.apply {
            div("ui middle aligned center aligned fullpage grid") {
                div("column") {
                    input(type = InputType.file) { id = "uploader" }
                    button(type = ButtonType.button, classes = "large ui violet button") {
                        onClick = "uploadFile(\"uploader\")"
                        text("Upload files")
                    }
                }
            }
        }
    }

}
