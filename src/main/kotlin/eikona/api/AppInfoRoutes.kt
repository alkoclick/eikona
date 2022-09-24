import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.appInfoRoutes() {
    route("/api/health") {
        get {
            call.respond("Ok!")
        }
    }
}
