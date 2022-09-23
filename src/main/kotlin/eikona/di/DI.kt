package eikona.di

import eikona.Config
import eikona.ConfigSchema.Storage.ObjectStorage.*
import eikona.ConfigSchema.Storage.RelationalStorage.*
import eikona.specs.AuthorizationStorageSpec
import eikona.specs.BlobStorageSpec
import eikona.storage.`object`.BlobStorageDisk
import eikona.storage.`object`.BlobStorageInMemory
import eikona.storage.relational.AuthorizationStorageAccessor

object DI {

    val blobStorage: BlobStorageSpec by lazy {
        when (val objectStore = Config.storage.objects) {
            is MapDBFile -> BlobStorageDisk(objectStore.filename)
            is InMemory -> BlobStorageInMemory
            is S3 -> TODO()
        }
    }

    val authStorage: AuthorizationStorageSpec by lazy {
        when (val relationalStorage = Config.storage.relational) {
            is Remote -> TODO()
            SQLiteFile, SQLiteInMemory -> AuthorizationStorageAccessor(relationalStorage)
        }
    }
}