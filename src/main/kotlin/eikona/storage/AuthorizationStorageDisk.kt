package eikona.storage

import eikona.specs.Authorization
import eikona.specs.AuthorizationStorageSpec
import eikona.specs.AuthorizationType
import eikona.specs.StorageResponse
import org.mapdb.DataInput2
import org.mapdb.DataOutput2
import org.mapdb.Serializer
import java.util.*

object AuthorizationStorageDisk : AuthorizationStorageSpec {

    private val blobs = StorageDisk.fileDb.hashMap(
        "authorizations", keySerializer = Serializer.UUID, valueSerializer = object : Serializer<Authorization> {
            override fun serialize(out: DataOutput2, value: Authorization) =
                out.writeUTF(
                    listOf(value.blobUUID, value.userUUID, value.authorizationType.toString()).joinToString(
                        separator = ","
                    )
                )

            override fun deserialize(input: DataInput2, available: Int): Authorization =
                input.readUTF().split(",").let {
                    Authorization(
                        UUID.fromString(it[0]),
                        UUID.fromString(it[1]),
                        AuthorizationType.valueOf(it[2])
                    )
                }

        }
    ).createOrOpen()

    override fun listByBlob(blobUUID: UUID) =
        StorageResponse.Ok(blobs.filterValues { it.blobUUID == blobUUID }.values)

    override fun canDo(blobUUID: UUID, userUUID: UUID, authorizationType: AuthorizationType): StorageResponse<Boolean> =
        StorageResponse.Ok(
            blobs.any {
                it.value.blobUUID == blobUUID && it.value.userUUID == userUUID && it.value.authorizationType.canDo(
                    authorizationType
                )
            }
        )

    override fun create(key: UUID, value: Authorization): StorageResponse<UUID> {
        blobs[key] = value
        return StorageResponse.Ok(key)
    }

    override fun read(key: UUID): StorageResponse<Authorization> =
        blobs[key]?.let { StorageResponse.Ok(it) } ?: StorageResponse.FileNotFound

    override fun delete(key: UUID): StorageResponse<Boolean> {
        blobs - key
        return StorageResponse.Ok(true)
    }
}