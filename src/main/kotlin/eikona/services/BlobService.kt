package eikona.services

import eikona.di.DI
import eikona.specs.Authorization
import eikona.specs.AuthorizationType
import eikona.specs.StorageResponse
import java.util.*

object BlobService {

    private val authService = DI.authStorage
    private val blobService = DI.blobStorage

    fun create(key: UUID, value: ByteArray, userUUID: UUID): StorageResponse<UUID> =
        blobService.create(key, value).also {
            authService.create(UUID.randomUUID(), Authorization(key, userUUID, AuthorizationType.ADMIN))
        }

    fun read(key: UUID, userUUID: UUID): StorageResponse<ByteArray> =
        authService.canDo(key, userUUID, AuthorizationType.READ)
            .takeIf { it is StorageResponse.Ok && it.content }
            ?.let { blobService.read(key) }
            ?: StorageResponse.FileNotFound

    fun delete(key: UUID, userUUID: UUID): StorageResponse<Boolean> =
        authService.canDo(key, userUUID, AuthorizationType.ADMIN)
            .takeIf { it is StorageResponse.Ok && it.content }
            ?.let { blobService.delete(key) }
            ?: StorageResponse.FileNotFound
}