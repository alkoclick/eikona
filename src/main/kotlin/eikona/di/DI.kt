package eikona.di

import eikona.specs.AuthorizationStorageSpec
import eikona.specs.BlobStorageSpec
import eikona.storage.AuthorizationStorageDisk
import eikona.storage.AuthorizationStorageInMemory
import eikona.storage.BlobStorageDisk
import eikona.storage.BlobStorageInMemory

object DI {

    val blobStorage: BlobStorageSpec by lazy { BlobStorageDisk }

    val authStorage: AuthorizationStorageSpec by lazy { AuthorizationStorageDisk }
}