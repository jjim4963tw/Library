package com.jjim4963tw.library.manager

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jjim4963tw.library.utility.MediaUtility

class CameraManager(val activity: AppCompatActivity) {
    interface CameraRequestListener {
        fun requestSuccess(uri: Uri)
        fun requestFail(status: Int)
    }

    private var listener: CameraRequestListener? = null
    private var mediaURI: Uri? = null

    /**
     * 註冊 Activity Result API Callback
     */
    private val takePictureLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            if (mediaURI != null) {
                listener?.requestSuccess(this.mediaURI!!)
            } else {
                listener?.requestFail(998)
            }
        } else {
            listener?.requestFail(999)
        }
    }

    private val takeVideoLauncher = activity.registerForActivityResult(ActivityResultContracts.CaptureVideo()) { isSuccess ->
        if (isSuccess) {
            if (mediaURI != null) {
                listener?.requestSuccess(this.mediaURI!!)
            } else {
                listener?.requestFail(998)
            }
        } else {
            listener?.requestFail(999)
        }
    }

    /**
     * 開啟相機拍照
     */
    fun requestCamera(listener: CameraRequestListener) {
        this.listener = listener

        MediaUtility.getStorageMediaPath(activity, MediaUtility.REQUEST_CAMERA_CODE).also {
            this.mediaURI = it
            this.takePictureLauncher.launch(it)
        }
    }

    /**
     * 開啟相機錄影
     */
    fun requestVideo(listener: CameraRequestListener) {
        this.listener = listener

        MediaUtility.getStorageMediaPath(activity, MediaUtility.REQUEST_VIDEO_CODE).also {
            this.mediaURI = it
            this.takeVideoLauncher.launch(it)
        }
    }
}