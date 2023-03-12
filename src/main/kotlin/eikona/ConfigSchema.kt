package eikona

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

val Config = ConfigLoaderBuilder.default()
    .addResourceSource("/application.conf")
    .addResourceSource("/application-test.conf", optional = true)
    .addResourceSource("/application-secrets.conf", optional = true)
    .build()
    .loadConfigOrThrow<ConfigSchema>()

data class ConfigSchema(
    val storage: Storage,
    val auth: AuthConfig,
) {

    data class Storage(
        val objects: ObjectStorage,
        val relational: RelationalStorage,
        val sessions: SessionsStorage
    ) {
        sealed class ObjectStorage {
            data class MapDBFile(val filename: String) : ObjectStorage()
            data class S3(val uri: String) : ObjectStorage()
            object InMemory: ObjectStorage()
        }

        sealed class RelationalStorage(
            open val url: String,
            open val driver: String,
        ) {
            data class Remote(
                override val url: String,
                override val driver: String,
            ) : RelationalStorage(url, driver)

            object SQLiteInMemory :
                RelationalStorage("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")

            object SQLiteFile : RelationalStorage("jdbc:sqlite:sqlite.db", "org.sqlite.JDBC")
        }

        sealed class SessionsStorage {
            data class DirectorySessionStorage(val filename: String): SessionsStorage()
            object MemorySessionStorage: SessionsStorage()
        }
    }

    data class AuthConfig(
        val audience: String,
        val client: String,
        val issuer: String,
    )
}