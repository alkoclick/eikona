package eikona.storage.`object`

import org.mapdb.DBMaker
import kotlin.concurrent.thread

object StorageDisk {
    fun makeFileDb(filename: String) = DBMaker.fileDB(filename).make().also {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            it.close()
        })
    }
}
