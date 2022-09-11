package eikona.specs

interface CRDStorageSpec<KEY, VALUE> {

    // Should be converted to stream rather than load eagerly
    fun create(key: KEY, value: VALUE): StorageResponse<KEY>

    fun read(key: KEY): StorageResponse<VALUE>

    fun delete(key: KEY): StorageResponse<Boolean>
}
