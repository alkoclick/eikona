package eikona.ui.pages

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.AuthenticatedPageTemplate
import io.ktor.server.application.*
import io.ktor.server.auth.*
import kotlinx.html.BODY
import kotlinx.html.DIV
import kotlinx.html.div

class CollectionPage(val call: ApplicationCall) : AuthenticatedPageTemplate {
    override val user: UserSessionPrincipal = call.principal()!!
    private val albums = listOf(
        Album("Family", "Pics of family", "https://www.christies.com/img/LotImages/2008/CSK/2008_CSK_05396_0310_000().jpg"),
        Album("Placeholder", "Pics of family", "https://1.bp.blogspot.com/-_SdmKLXanso/VDrCPhhIOjI/AAAAAAAAJts/D_mXnrP_mFI/s1600/tillmans-freischwimmer-102-2004.jpg"),
        Album("That other thing", "Pics of family", "https://i1.wp.com/saramunari.blog/wp-content/uploads/2016/12/maureen-paley-wolfgang-tillmans-artwork-i-dont-want-to-get-over-you-2000.jpg"),
        Album("Old", "", ""),
        Album("Untitled", "", null),
    )
    override val additionalCssHref: String = "/assets/css/pages/collection.css"

    override fun renderBody(body: BODY) {
        body.div("px-4 py-5 my-5 text-center container") {
            uploadButton()

            div(classes = "album py-5") {
                div(classes = "container") {
                    div(classes = "row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 g-1") {
                        albums.forEach { photoPreview(it) }
                    }
                }
            }
        }
    }

    private fun DIV.photoPreview(album: Album) {
        div(classes = "col") {
            div(classes = "card shadow-sm") {
                div(classes = "media-preview") {
                    coverImage(album)
                }
            }
        }
    }
}