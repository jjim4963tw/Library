package com.jjim4963tw.library.manager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionManager(val activity: AppCompatActivity) {
    interface PermissionRequestListener {
        fun requestSuccess()
        fun requestFail(deniedPermissions: ArrayList<String>)
    }

    private var listener: PermissionRequestListener? = null

    /**
     * 註冊 Activity Result API Callback
     */
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val deniedPermissions = arrayListOf<String>()
        permissions.forEach {
            if (!it.value) {
                deniedPermissions.add(it.key)
            }
        }

        listener?.let {
            if (deniedPermissions.size <= 0) {
                it.requestSuccess()
            } else {
                it.requestFail(deniedPermissions)
            }
        }
    }

    /**
     * 檢查是否已賦予權限
     */
    fun hasPermission(permissions: Array<String>): Boolean {
        var isAccess = true
        permissions.forEach {
            if (activity.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                isAccess = false
                return@forEach
            }
        }

        return isAccess
    }

    /**
     * 要求權限
     */
    fun requestPermission(permissions: Array<String>, listener: PermissionRequestListener) {
        this.listener = listener
        this.launcher.launch(permissions)
    }

    /**
     * 跳轉到設定的應用程式頁
     */
    fun goAppSettingsPage() {
        activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.packageName, null)).run {
            this.addCategory(Intent.CATEGORY_DEFAULT)
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this
        })
    }

    companion object {
        /**
         * Android 13 開始，WRITE_EXTERNAL_STORAGE、READ_EXTERNAL_STORAGE 在詢問權限時都將一律回傳 false。
         */
        val mediaStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val mediaStorageWithoutAudioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
        }

        val oldStoragePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        val cameraPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA)
        } else {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        }
    }
}