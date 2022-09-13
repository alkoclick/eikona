package eikona.storage.relational.table

import org.jetbrains.exposed.dao.id.IntIdTable

object AuthorizationsTable : IntIdTable() {
    val authorizationTypeLevel = varchar("authorization_level", length = 20)
    val blobUUID = uuid("blob_uuid").index()
    val userUUID = uuid("user_uuid")
}