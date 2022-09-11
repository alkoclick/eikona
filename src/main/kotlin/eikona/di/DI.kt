package eikona.di

import eikona.specs.AuthorizationStorageSpec
import eikona.specs.BlobStorageSpec
import eikona.storage.AuthorizationStorageInMemory
import eikona.storage.BlobStorageInMemory

object DI {

    val blobStorage: BlobStorageSpec by lazy { BlobStorageInMemory }

    val authStorage: AuthorizationStorageSpec by lazy { AuthorizationStorageInMemory }
}