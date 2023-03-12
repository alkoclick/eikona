package eikona.api

import eikona.services.BlobService
import eikona.specs.StorageResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

fun Route.blobRouting() {

    authenticate("auth-session") {

        route("/api/blob") {
            post {
                if (call.request.header("Content-Type") == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val userUUID = UUID.fromString(call.principal<UserSessionPrincipal>()?.name.toString())
                // retrieve all bytes (suspending)
                val bytes = withContext(Dispatchers.IO) { call.receiveStream().readAllBytes() }
                when (val storageResponse = BlobService.create(UUID.randomUUID(), bytes, userUUID)) {
                    is StorageResponse.Invalid -> call.respond(HttpStatusCode.BadRequest,
                        "{ \"Message\" : \"${storageResponse}\" }")
                    is StorageResponse.Ok -> call.respond("{ \"UUID\" : \"${storageResponse.content}\" }")
                }
            }

            get("{id?}") {
                val userUUID = UUID.fromString(call.principal<UserSessionPrincipal>()?.name.toString())
                when (val storageResponse = BlobService.read(UUID.fromString(call.parameters["id"]), userUUID)) {
                    is StorageResponse.FileNotFound -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                    is StorageResponse.Invalid -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                    is StorageResponse.Ok -> call.respondBytes(
                        contentType = ContentType.Image.JPEG,
                        bytes = storageResponse.content
                    )
                }
            }

            delete("{id?}") {
                val userUUID = UUID.fromString(call.principal<UserSessionPrincipal>()?.name.toString())
                when (val storageResponse = BlobService.delete(UUID.fromString(call.parameters["id"]), userUUID)) {
                    is StorageResponse.FileNotFound -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                    is StorageResponse.Invalid -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                    is StorageResponse.Ok -> call.respond(storageResponse.toString())
                }
            }
        }
    }
}