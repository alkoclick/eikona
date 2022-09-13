package eikona.di

import eikona.specs.AuthorizationStorageSpec
import eikona.specs.BlobStorageSpec
import eikona.storage.relational.AuthorizationStorageAccessor
import eikona.storage.`object`.BlobStorageDisk

object DI {

    val blobStorage: BlobStorageSpec by lazy { BlobStorageDisk }

    val authStorage: AuthorizationStorageSpec by lazy { AuthorizationStorageAccessor }
}