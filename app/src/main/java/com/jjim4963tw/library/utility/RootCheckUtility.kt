package com.jjim4963tw.library.utility

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class RootCheckUtility {
    companion object {
        @JvmStatic
        fun isDeviceRooted(context: Context): Boolean = (checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4(context))

        private fun checkRootMethod1(): Boolean = (Build.TAGS != null && Build.TAGS.contains("test-keys"))

        private fun checkRootMethod2(): Boolean {
            arrayOf("/system/app/Superuser/", "/system/app/Superuser.apk", "/system/app/SuperSu/", "/system/app/SuperSu.apk",
                    "/system/app/Magisk Manager.apk", "/system/app/Magisk Manager/", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                    "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su")
                    .forEach {
                        if (File(it).exists()) return true
                    }
            return false
        }

        private fun checkRootMethod3(): Boolean {
            var process: Process? = null
            return try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                process.run {
                    InputStreamReader(this.inputStream).run {
                        BufferedReader(this)
                    }
                }.readLine() != null
            } catch (t: Throwable) {
                false
            } finally {
                process?.destroy()
            }
        }

        private fun checkRootMethod4(context: Context): Boolean {
            context.packageManager.run {
                this.getInstalledPackages(0)
            }.forEach { packageInfo ->
                if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) <= 0 &&
                        (packageInfo.packageName == "com.topjohnwu.magisk" || packageInfo.packageName == "eu.chainfire.supersu" ||
                                (packageInfo.applicationInfo.className != null && packageInfo.applicationInfo.className == "a.e"))) {
                    return true
                }
            }
            return false
        }
    }
}