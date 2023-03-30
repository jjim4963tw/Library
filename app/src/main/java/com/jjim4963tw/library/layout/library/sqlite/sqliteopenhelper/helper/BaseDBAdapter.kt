package com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.helper

import android.database.sqlite.SQLiteDatabase
import com.jjim4963tw.library.Library
import com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.MyDatabaseOpenHelper

open class BaseDBAdapter {
    var database: MyDatabaseOpenHelper = MyDatabaseOpenHelper

    var db: SQLiteDatabase? = null

    fun openDatabase(): SQLiteDatabase {
        synchronized(this) {
            return if (Library.atomicInteger.incrementAndGet() == 1 || db == null) {
                database.writableDatabase.also {
                    db = it
                }
            } else {
                db!!
            }
        }
    }

    fun closeDatabase() {
        synchronized(this) {
            if (Library.atomicInteger.incrementAndGet() == 0) {
                db?.close()
            }
        }
    }
}