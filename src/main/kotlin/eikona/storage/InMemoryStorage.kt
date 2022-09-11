package eikona.storage

import eikona.specs.StorageResponse
import eikona.specs.StorageSpec
import java.util.*

class InMemoryStorage : StorageSpec {

    val blobs = mutableMapOf<UUID, ByteArray>()

    override fun addBlob(uid: UUID, blob: ByteArray): StorageResponse<Boolean> {
        blobs[uid] = blob
        return StorageResponse.Ok(true)
    }

    override fun getBlob(uid: UUID): StorageResponse<ByteArray> =
        blobs[uid]?.let { StorageResponse.Ok(it) } ?: StorageResponse.FileNotFound

    override fun listBlobs(): StorageResponse<Collection<UUID>> =
        StorageResponse.Ok(blobs.keys)

    override fun removeBlob(uid: UUID): StorageResponse<Boolean> {
        blobs - uid
        return StorageResponse.Ok(true)
    }
}
