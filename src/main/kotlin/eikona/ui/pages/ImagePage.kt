package eikona.ui.pages

import eikona.ui.templates.UIPage
import io.ktor.server.application.*
import kotlinx.html.*

class ImagePage(val call: ApplicationCall) : UIPage {

    override fun HTML.render() {
        val imgUuid = call.parameters["id"]
        head {
            title = "Eikona"
        }
        body {
            div("container") {
                text("Image: $imgUuid")
                img(src = "/api/blob/$imgUuid")
            }
        }
    }

}