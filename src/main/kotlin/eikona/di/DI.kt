package eikona.di

import eikona.specs.StorageSpec
import eikona.storage.InMemoryStorage

object DI {

    val storageDriver: StorageSpec by lazy { InMemoryStorage() }
}