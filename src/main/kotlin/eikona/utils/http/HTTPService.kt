package eikona.utils.http

import eikona.utils.http.HTTPRequestMethod.*
import eikona.utils.http.HTTPResponse.Error
import eikona.utils.http.HTTPResponse.Received
import eikona.utils.logger.Logger
import eikona.utils.metrics.Metrics.HTTPRequests.httpRequests
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/**
 * The basic object for connections, abstracts the underlying http connection framework.
 * It also provides a single place to make changes across all HTTP requests, if needed.
 */
object HTTPService {

    private val client: OkHttpClient = OkHttpClient()
    private val idRegex by lazy { "(/\\d+)+?".toRegex() }
    private val queryParamRegex by lazy { "(\\?.*)".toRegex() }
    private val logger = Logger(javaClass)

    /**
     * Implements a head call with no body
     *
     * Catching any exception here is up to the consumer
     */
    fun head(url: String): HTTPResponse =
        executeRequest(url, HEAD, null)

    /**
     * Implements a get call with no body
     *
     * Catching any exception here is up to the consumer
     */
    fun get(url: String, vararg headers: Pair<String, String>): HTTPResponse =
        executeRequest(url, GET, null, headers = headers)

    /**
     * Implements a put call with a body
     *
     * Catching any exception here is up to the consumer
     */
    fun put(url: String, body: String, vararg headers: Pair<String, String>): HTTPResponse =
        executeRequest(url, PUT, body, *headers)

    /**
     * Implements a post call with a body
     *
     * Catching any exception here is up to the consumer
     */
    fun post(url: String, body: String, vararg headers: Pair<String, String>): HTTPResponse =
        executeRequest(url, POST, body, *headers)

    /**
     * Implements a delete call
     *
     * Catching any exception here is up to the consumer
     */
    fun delete(url: String, vararg headers: Pair<String, String>): HTTPResponse =
        executeRequest(url, DELETE, null, *headers)

    /**
     * A function that executes a request and takes care of creating metrics about it.
     */
    private fun executeRequest(url: String, method: HTTPRequestMethod, body: String? = null, vararg headers: Pair<String, String>): HTTPResponse {
        lateinit var response: HTTPResponse

        val sanitizedURL = removeIdsFromPathForMetrics(url)
        logger.debug("Executing $method request to $sanitizedURL")
        val elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(
            measureTimeMillis {
                response = flow(url = url, method = method, body = body, headers = headers)
            }
        )

        val statusCode = when (response) {
            is Received -> (response as Received).code.toString()
            is Error -> "-1"
        }
        httpRequests.labels(sanitizedURL, method.name.lowercase(), statusCode).observe(elapsedSeconds.toDouble())

        return response
    }

    /**
     * A wrapper around creating a request and executing it
     *
     * If you want to add any custom functionality, such as coroutine wrappers, this is the place
     */
    private fun flow(url: String, method: HTTPRequestMethod, body: String? = null, vararg headers: Pair<String, String>): HTTPResponse =
        runCatching {
            // If you check the implem it's pretty simple/inefficient (using try-catch) but we use a call
            // since they may someday change it to a better one (and we'd be doing the same here anyway)
            if (url.toHttpUrlOrNull() == null)
                return@runCatching Error("Illegal url supplied: $url", null, url, method)

            val request = request(url, method.adaptToOkHttpMethod(), body, headers.toList())

            client
                .newCall(request)
                .execute()
                .use { response -> Received(response.code, response.body?.string(), url, method) }
        }.getOrElse { throwable ->
            Error(throwable, url, method)
        }

    private fun request(url: String, method: String, body: String? = null, headers: List<Pair<String, String>>) =
        Request.Builder()
            .url(url)
            .method(method, body?.toRequestBody())
            .also { request -> headers.forEach { header -> request.addHeader(header.first, header.second) } }
            .build()

    internal fun removeIdsFromPathForMetrics(url: String): String =
        url.removePrefix("https://")
            .removePrefix("http://")
            .trim()
            .replace(idRegex, "/{int}")
            .replace(queryParamRegex, "")

}
