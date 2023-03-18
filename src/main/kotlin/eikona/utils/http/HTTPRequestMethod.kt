package eikona.utils.http

/**
 * Represents the type of request used for http(s)
 */
enum class HTTPRequestMethod {

    HEAD,
    GET,
    PUT,
    POST,
    DELETE;

    /**
     * Currently works with OkHTTP method names
     *
     * When implementing another framework, this should be changed
     */
    fun adaptToOkHttpMethod(): String =
        name
}