package eikona.ui.pages

import eikona.api.UserSessionPrincipal
import eikona.ui.templates.AuthenticatedPageTemplate
import kotlinx.html.*
import java.util.*

class HomePage(override val user: UserSessionPrincipal) : AuthenticatedPageTemplate {
    private val albums = listOf(
        Album("Family", "Pics of family", "https://www.christies.com/img/LotImages/2008/CSK/2008_CSK_05396_0310_000().jpg"),
        Album("Placeholder", "Pics of family", "https://1.bp.blogspot.com/-_SdmKLXanso/VDrCPhhIOjI/AAAAAAAAJts/D_mXnrP_mFI/s1600/tillmans-freischwimmer-102-2004.jpg"),
        Album("That other thing", "Pics of family", "https://i1.wp.com/saramunari.blog/wp-content/uploads/2016/12/maureen-paley-wolfgang-tillmans-artwork-i-dont-want-to-get-over-you-2000.jpg"),
        Album("Old", "", ""),
        Album("Untitled", "", null),
    )
    override val additionalCssHref: String = "/assets/css/pages/home.css"

    override fun renderBody(body: BODY) {
        body.div("px-4 py-5 my-5 text-center container") {
            uploadButton()

            div(classes = "album py-5 bg-body-tertiary") {
                div(classes = "container") {
                    div(classes = "row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3") {
                        albums.forEach { album(it) }
                    }
                }
            }
        }
    }

    private fun DIV.album(album: Album) {
        div(classes = "col") {
            div(classes = "card shadow-sm") {
                div(classes = "album-preview") {
                    coverImage(album)
                }
                div(classes = "card-body") {
                    p(classes = "card-text") { text(album.name) }
                    a(href = "/collection/${album.uuid}", classes = "stretched-link")
                }
            }
        }
    }

}

fun DIV.coverImage(album: Album) {
    if (album.coverHref.isNullOrBlank()) {
        SVG(attributesMapOf("classes", "bi bi-image", "fill", "currentColor", "viewBox", "0 0 16 16"), consumer).visit {
            path(d = "M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z")
            path(d = "M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z")
        }
    } else {
        img(src = album.coverHref, classes = "card-img-top")
    }
}


data class Album(
    val name: String,
    val description: String,
    val coverHref: String?,
    val uuid: UUID = UUID.randomUUID()
)