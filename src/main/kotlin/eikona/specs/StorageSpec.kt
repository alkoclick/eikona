package eikona.specs

import java.util.*

interface StorageSpec {

    // Should be converted to stream rather than load eagerly
    fun addBlob(uid: UUID, blob: ByteArray) : StorageResponse<Boolean>

    fun getBlob(uid: UUID): StorageResponse<ByteArray>

    fun listBlobs(): StorageResponse<Collection<UUID>>

    fun removeBlob(uid: UUID) : StorageResponse<Boolean>
}
