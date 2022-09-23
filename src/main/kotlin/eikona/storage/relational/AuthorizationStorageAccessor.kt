package eikona.storage.relational

import eikona.ConfigSchema
import eikona.specs.Authorization
import eikona.specs.AuthorizationStorageSpec
import eikona.specs.AuthorizationType
import eikona.specs.StorageResponse
import eikona.storage.`object`.StorageDisk
import eikona.storage.relational.table.AuthorizationsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mapdb.DataInput2
import org.mapdb.DataOutput2
import org.mapdb.Serializer
import java.util.*

class AuthorizationStorageAccessor(relationalStorage: ConfigSchema.Storage.RelationalStorage) : AuthorizationStorageSpec {

    init {
        SQLDatabase.createConnection(relationalStorage.url, relationalStorage.driver)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(AuthorizationsTable)
        }
    }

    override fun canDo(blobUUID: UUID, userUUID: UUID, authorizationType: AuthorizationType): StorageResponse<Boolean> =
        transaction {
            AuthorizationsTable
                .select { AuthorizationsTable.blobUUID eq blobUUID and (AuthorizationsTable.userUUID eq userUUID) }
                .map { AuthorizationType.valueOf(it[AuthorizationsTable.authorizationTypeLevel]) }
        }
            .firstOrNull()
            ?.let { StorageResponse.Ok(it.canDo(authorizationType)) }
            ?: StorageResponse.FileNotFound


    override fun create(key: UUID, value: Authorization): StorageResponse<UUID> {
        transaction {
            AuthorizationsTable.insert {
                it[blobUUID] = value.blobUUID
                it[userUUID] = value.userUUID
                it[authorizationTypeLevel] = value.authorizationType.toString()
            }
        }
        return StorageResponse.Ok(key)
    }

    override fun read(key: UUID): StorageResponse<Authorization> =
        transaction {
            AuthorizationsTable.select { AuthorizationsTable.blobUUID eq key }.map {
                Authorization(
                    it[AuthorizationsTable.blobUUID],
                    it[AuthorizationsTable.userUUID],
                    AuthorizationType.valueOf(it[AuthorizationsTable.authorizationTypeLevel])
                )
            }
        }.firstOrNull()?.let { StorageResponse.Ok(it) } ?: StorageResponse.FileNotFound

    override fun delete(key: UUID): StorageResponse<Boolean> {
        transaction { AuthorizationsTable.deleteWhere { AuthorizationsTable.blobUUID eq key } }
        return StorageResponse.Ok(true)
    }
}