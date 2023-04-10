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
            div("px-4 py-5 my-5 text-center") {
                div("col-lg-6 mx-auto") {
                    // TODO Rebuild this functionality
                    // input(type = InputType.file) { id = "uploader" }
                    button(type = ButtonType.button, classes = "btn btn-lg btn-primary mb-3") {
                        onClick = "uploadFile(\"uploader\")"
                        text("Upload files")
                    }
                }
            }
        }
    }

}
