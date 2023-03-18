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
    val auth: Auth,
    val logger: Logger,
    val storage: Storage,
) {

    data class Auth(
        val audience: String,
        val client_id: String,
        val issuer: String,
        val secret: String,
        val endpoints: AuthEndpoints,
    ) {
        data class AuthEndpoints(
            val authorize: String,
            val token: String,
        )
    }

    data class Logger(
        val base_level: String,
        val filters: Map<String, String>
    )

    data class Storage(
        val objects: ObjectStorage,
        val relational: RelationalStorage,
        val sessions: SessionsStorage
    ) {
        sealed class ObjectStorage {
            data class MapDBFile(val filename: String) : ObjectStorage()
            data class S3(val uri: String) : ObjectStorage()
            object InMemory : ObjectStorage()
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
            data class DirectorySessionStorage(val filename: String) : SessionsStorage()
            object MemorySessionStorage : SessionsStorage()
        }
    }

}