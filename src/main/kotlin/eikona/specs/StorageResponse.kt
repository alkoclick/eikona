package eikona.specs


sealed class StorageResponse<out T> {

    abstract class Invalid(val message: String) : StorageResponse<Nothing>()

    object FileNotFound : StorageResponse.Invalid("The requested file could not be found")

    object AuthenticationFailed : StorageResponse.Invalid("The storage endpoint could not be reached")

    object StorageUnreachable : StorageResponse.Invalid("The storage endpoint could not be reached")

    data class Ok<T>(
        val content: T
    ) : StorageResponse<T>()
}
