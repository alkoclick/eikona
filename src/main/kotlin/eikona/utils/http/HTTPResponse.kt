package eikona.utils.http

/**
 *
 */
sealed class HTTPResponse(
    /**
     * The originally requested URL
     */
    open val requestUrl: String,
    open val method: HTTPRequestMethod
) {

    /**
     * An HTTP request response which may indicate server-side
     * success (2xx codes) or failure (4xx,5xx)
     */
    data class Received(
        /**
         * The HTTP status code received
         */
        val code: Int,

        /**
         * The body received, if any, as UTF-8 string
         */
        val body: String?,
        override val requestUrl: String,
        override val method: HTTPRequestMethod
    ) : HTTPResponse(requestUrl, method)

    /**
     * Indicates an unexpected failure.
     *
     * The request either never left Eikona, or if it did, there
     * was an error when receiving the response. We do not have
     * access to the response.
     *
     * Invalid URLs, network timeouts and other similar issues
     * fall here
     */
    data class Error(
        /**
         * A short human-readable description of the problem
         */
        val message: String,
        /**
         * The exception, if available
         */
        val cause: Throwable? = null,
        override val requestUrl: String,
        override val method: HTTPRequestMethod
    ) : HTTPResponse(requestUrl, method) {

        constructor(throwable: Throwable, requestUrl: String, method: HTTPRequestMethod) : this(throwable.message.orEmpty(), throwable, requestUrl, method)
    }
}