package com.jjim4963tw.library.layout.library.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jjim4963tw.library.layout.library.room.entity.UserDao
import com.jjim4963tw.library.layout.library.room.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class MyDataBase : RoomDatabase() {
    companion object {
        private var instance: MyDataBase? = null
        private var DB_NAME = "my_database"

        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_info ADD COLUMN address TEXT")
            }
        }

        fun getInstance(context: Context): MyDataBase {
            return instance ?: synchronized(MyDataBase::class) {
                Room.databaseBuilder(context, MyDataBase::class.java, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }
        }
    }

    abstract fun userDao(): UserDao
}