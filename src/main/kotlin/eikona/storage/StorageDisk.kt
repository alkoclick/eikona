package eikona.storage

import org.mapdb.DBMaker
import kotlin.concurrent.thread

object StorageDisk {
    val fileDb = DBMaker.fileDB("mapDb").make().also {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            it.close()
        })
    }
}