package eikona.di

import eikona.Config
import eikona.ConfigSchema
import eikona.ConfigSchema.Storage.ObjectStorage.*
import eikona.ConfigSchema.Storage.RelationalStorage.*
import eikona.ConfigSchema.Storage.SessionsStorage.DirectorySessionStorage
import eikona.ConfigSchema.Storage.SessionsStorage.MemorySessionStorage
import eikona.specs.AuthorizationStorageSpec
import eikona.specs.BlobStorageSpec
import eikona.storage.`object`.BlobStorageDisk
import eikona.storage.`object`.BlobStorageInMemory
import eikona.storage.relational.AuthorizationStorageAccessor
import io.ktor.server.sessions.*
import java.io.File

object DI {

    val sessionStorage: SessionStorage by lazy {
        when (val sessionStore = Config.storage.sessions) {
            is DirectorySessionStorage -> directorySessionStorage(File(sessionStore.filename))
            is MemorySessionStorage -> SessionStorageMemory()
        }
    }

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