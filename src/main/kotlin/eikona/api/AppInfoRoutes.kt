import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.appInfoRoutes() {
    route("/api/health") {
        get {
            call.respond("Ok!")
        }
    }
    authenticate("auth0") {
        get("/api/health-protected") {
            call.respondText(
                """{"message": "The API successfully validated your access token."}""",
                contentType = ContentType.Application.Json
            )
        }
    }
}
