package eikona.storage

import eikona.specs.StorageResponse
import eikona.specs.BlobStorageSpec
import java.util.*

object BlobStorageInMemory : BlobStorageSpec {

    private val blobs = mutableMapOf<UUID, ByteArray>()

    override fun create(key: UUID, value: ByteArray): StorageResponse<UUID> {
        blobs[key] = value
        return StorageResponse.Ok(key)
    }

    override fun read(key: UUID): StorageResponse<ByteArray> =
        blobs[key]?.let { StorageResponse.Ok(it) } ?: StorageResponse.FileNotFound

    override fun delete(key: UUID): StorageResponse<Boolean> {
        blobs.remove(key)
        return StorageResponse.Ok(true)
    }
}
