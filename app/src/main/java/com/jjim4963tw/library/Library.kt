package com.jjim4963tw.library

import android.content.pm.ApplicationInfo
import androidx.multidex.MultiDexApplication

class Library: MultiDexApplication() {
    companion object {
        var isDebugMode = false
    }

    override fun onCreate() {
        super.onCreate()

        isDebugMode = 0 != ApplicationInfo.FLAG_DEBUGGABLE.let {
            applicationInfo.flags = applicationInfo.flags and it; applicationInfo.flags
        }

    }
}