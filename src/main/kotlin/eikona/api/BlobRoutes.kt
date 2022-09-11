package eikona.api

import eikona.services.BlobService
import eikona.specs.StorageResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.blobRouting() {

    route("/blob") {
        post {
            if (call.request.header("User") == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            if (call.request.header("Content-Type") == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userUUID = UUID.fromString(call.request.header("User"))
            // retrieve all multipart data (suspending)
            val multipart = call.receiveMultipart()
            var uploadedParts = 0
            val keys = mutableListOf<UUID>()
            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if (part is PartData.FileItem) {
                    val bytes = part.streamProvider().readBytes()
                    BlobService.create(UUID.randomUUID(), bytes, userUUID).also {
                        if (it is StorageResponse.Ok) keys += it.content
                    }
                    uploadedParts++
                }
                // make sure to dispose of the part after use to prevent leaks
                part.dispose()
            }
            call.respond("Completed $uploadedParts uploads and uploaded $keys")
        }

        get("{id?}") {
            if (call.request.header("User") == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val userUUID = UUID.fromString(call.request.header("User"))
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
            if (call.request.header("User") == null)
                call.respond(HttpStatusCode.Unauthorized)

            val userUUID = UUID.fromString(call.request.header("User"))
            when (val storageResponse = BlobService.delete(UUID.fromString(call.parameters["id"]), userUUID)) {
                is StorageResponse.FileNotFound -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                is StorageResponse.Invalid -> call.respond(HttpStatusCode.NotFound, storageResponse.message)
                is StorageResponse.Ok -> call.respond(storageResponse.toString())
            }
        }
    }
}