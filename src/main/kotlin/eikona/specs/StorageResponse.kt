package eikona.specs


sealed class StorageResponse<out T> {

    abstract class Invalid(val message: String) : StorageResponse<Nothing>() {
        override fun toString(): String {
            return message
        }
    }

    object FileNotFound : Invalid("The requested file could not be found")

    object StorageUnreachable : Invalid("The storage endpoint could not be reached")

    data class Ok<T>(
        val content: T
    ) : StorageResponse<T>()
}
