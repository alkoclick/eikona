package eikona.storage.relational

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

object SQLDatabase {

    val connection by lazy {
        Database.connect(url = "jdbc:sqlite:sqlite.db", driver = "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel =
            Connection.TRANSACTION_SERIALIZABLE
    }
}