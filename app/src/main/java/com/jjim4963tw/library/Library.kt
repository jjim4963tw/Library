package com.jjim4963tw.library

import android.content.pm.ApplicationInfo
import androidx.multidex.MultiDexApplication
import com.jjim4963tw.library.sqlite.sqliteopenhelper.MyDatabaseOpenHelper
import java.util.concurrent.atomic.AtomicInteger

class Library : MultiDexApplication() {
    companion object {
        var isDebugMode = false
        var atomicInteger = AtomicInteger()

        lateinit var instance: Library
            private set
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        isDebugMode = 0 != ApplicationInfo.FLAG_DEBUGGABLE.let {
            applicationInfo.flags = applicationInfo.flags and it; applicationInfo.flags
        }

    }
}