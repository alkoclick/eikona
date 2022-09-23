package eikona.storage.relational

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

object SQLDatabase {

    fun createConnection(url: String, driver: String) {
        Database.connect(url, driver)
        TransactionManager.manager.defaultIsolationLevel =
            Connection.TRANSACTION_SERIALIZABLE
    }
}