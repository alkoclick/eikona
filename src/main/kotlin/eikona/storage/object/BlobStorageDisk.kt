package eikona.storage.`object`

import eikona.specs.StorageResponse
import eikona.specs.BlobStorageSpec
import org.mapdb.Serializer
import java.util.*

class BlobStorageDisk(filename: String) : BlobStorageSpec {

    private val blobs = StorageDisk.makeFileDb(filename).hashMap(
        "blob", keySerializer = Serializer.UUID, valueSerializer = Serializer.BYTE_ARRAY
    ).createOrOpen()

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
