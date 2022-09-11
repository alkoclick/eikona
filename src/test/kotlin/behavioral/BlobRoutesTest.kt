package behavioral

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BlobRoutesTest {

    private val fileToUpload = File("src/test/resources/wiki.png")
    private val uuidRegex = Regex("\\[([A-z\\d-]{36})]")
    private val userUUID = "7e0f20e0-0b72-4a11-9ec0-a8c8a377cdaf"
    private val userOtherUUID = "699a4501-2ec1-44ec-8854-10dc92709d1c"

    private val boundary = "WebAppBoundary"
    private val multipartBody = MultiPartFormDataContent(
        formData {
            append("description", "Ktor logo")
            append("image", fileToUpload.readBytes(), Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=\"${fileToUpload.name}\"")
            })
        },
        boundary,
        ContentType.MultiPart.FormData.withParameter("boundary", boundary)
    )

    @Test
    fun uploadFile() = testApplication {
        val response = client.post("/blob") {
            header("User", userUUID)
            setBody(multipartBody)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue { response.bodyAsText().contains(uuidRegex) }
    }

    @Test
    fun uploadNoFile() = testApplication {
        val response = client.post("/blob") {
            header("User", userUUID)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun uploadFileWithoutUser() = testApplication {
        val response = client.post("/blob") {
            setBody(multipartBody)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun uploadFileAndReadList() = testApplication {
        val blobUuid = uuidRegex.find(
            client.post("/blob") {
                header("User", userUUID)
                setBody(multipartBody)
            }.bodyAsText()
        )?.groupValues?.get(1)

        val response = client.get("/blob/$blobUuid") {
            header("User", userUUID)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Image.JPEG, response.contentType())
    }

    @Test
    fun deleteNonExistentFile() = testApplication {
        val response = client.delete("/blob/${UUID.randomUUID()}") {
            header("User", userUUID)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("The requested file could not be found", response.bodyAsText())
    }

    @Test
    fun deleteExistingFile() = testApplication {
        val blobUuid = uuidRegex.find(
            client.post("/blob") {
                header("User", userUUID)
                setBody(multipartBody)
            }.bodyAsText()
        )?.groupValues?.get(1)

        val response = client.delete("/blob/$blobUuid") {
            header("User", userUUID)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Ok(content=true)", response.bodyAsText())
    }

    @Test
    fun readUnauthorizedFile() = testApplication {
        val blobUuid = uuidRegex.find(
            client.post("/blob") {
                header("User", userUUID)
                setBody(multipartBody)
            }.bodyAsText()
        )?.groupValues?.get(1)

        val response = client.get("/blob/$blobUuid") {
            header("User", userOtherUUID)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("The requested file could not be found", response.bodyAsText())
    }

    @Test
    fun deleteUnauthorizedFile() = testApplication {
        val blobUuid = uuidRegex.find(
            client.post("/blob") {
                header("User", userUUID)
                setBody(multipartBody)
            }.bodyAsText()
        )?.groupValues?.get(1)

        val response = client.delete("/blob/$blobUuid") {
            header("User", userOtherUUID)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("The requested file could not be found", response.bodyAsText())
    }

}