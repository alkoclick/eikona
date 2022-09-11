package eikona.storage

import eikona.specs.Authorization
import eikona.specs.AuthorizationStorageSpec
import eikona.specs.AuthorizationType
import eikona.specs.StorageResponse
import java.util.UUID

object AuthorizationStorageInMemory : AuthorizationStorageSpec {

    private val blobs = mutableMapOf<UUID, Authorization>()

    override fun listByBlob(blobUUID: UUID) =
        StorageResponse.Ok(blobs.filterValues { it.blobUUID == blobUUID }.values)

    override fun canDo(blobUUID: UUID, userUUID: UUID, authorizationType: AuthorizationType): StorageResponse<Boolean> =
        StorageResponse.Ok(
            blobs.any { it.value.blobUUID == blobUUID && it.value.userUUID == userUUID && it.value.authorizationType.canDo(authorizationType) }
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