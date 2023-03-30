package com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jjim4963tw.library.Library

object MyDatabaseOpenHelper: SQLiteOpenHelper(Library.instance, "my_database", null, 1) {
    object TableShell {
        const val TABLE_USER_INFO = """
            create table IF NOT EXISTS user_info (
                username text default '',
                age integer default 0,
                address text default '',
                id integer primary key autoincrement
            );
            """
    }

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(TableShell.TABLE_USER_INFO)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}