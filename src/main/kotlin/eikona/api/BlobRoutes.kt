package eikona.api

import eikona.di.DI
import eikona.specs.StorageResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.blobRouting() {
    val blobService = DI.storageDriver

    route("/blob") {
        post {
            // retrieve all multipart data (suspending)
            val multipart = call.receiveMultipart()
            var uploadedParts = 0
            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if(part is PartData.FileItem) {
                    val bytes = part.streamProvider().readBytes()
                    blobService.addBlob(UUID.randomUUID(), bytes)
                    uploadedParts++
                }
                // make sure to dispose of the part after use to prevent leaks
                part.dispose()
            }
            call.respond("Completed $uploadedParts uploads")
        }

        get("{id?}") {
            when (val storageResponse = blobService.getBlob(UUID.fromString(call.parameters["id"]))) {
                is StorageResponse.Invalid -> call.respond(storageResponse.message)
                is StorageResponse.Ok -> call.respondBytes(contentType = ContentType.Image.JPEG, bytes = storageResponse.content)
            }
        }

        delete("{id?}") {
            blobService.removeBlob(UUID.fromString(call.parameters["id"]))
        }
    }

    get("/blob/list") {
        call.respond(blobService.listBlobs().toString())
    }
}