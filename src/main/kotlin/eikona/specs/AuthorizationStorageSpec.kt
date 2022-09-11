package eikona.specs

import java.util.UUID

interface AuthorizationStorageSpec : CRDStorageSpec<UUID, Authorization> {

    fun listByBlob(blobUUID: UUID): StorageResponse<Collection<Authorization>>

    fun canDo(blobUUID: UUID, userUUID: UUID, authorizationType: AuthorizationType): StorageResponse<Boolean>
}

data class Authorization(
    val blobUUID: UUID,
    val userUUID: UUID,
    val authorizationType: AuthorizationType = AuthorizationType.NONE
)

enum class AuthorizationType(val level: Int) {
    NONE(0),
    READ(10),
    READ_WRITE(100),
    ADMIN(1000),;

    fun canDo(requestedAuth: AuthorizationType): Boolean {
        return level >= requestedAuth.level
    }
}