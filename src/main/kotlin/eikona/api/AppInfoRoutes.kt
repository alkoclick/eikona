import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.appInfoRoutes() {
    route("/health") {
        get {
            call.respond("Ok!")
        }
    }
}
