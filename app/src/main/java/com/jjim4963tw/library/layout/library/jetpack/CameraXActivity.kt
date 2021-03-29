package com.jjim4963tw.library.layout.library.jetpack

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import com.jjim4963tw.library.R


class CameraXActivity : AppCompatActivity() {
    companion object {
        private val TAG = CameraXActivity::class.simpleName
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)

        findViewById<Button>(R.id.btn_take_picture).setOnClickListener {
            this.takePictureFunction()
        }

        // 檢查相機權限
        if (allPermissionGranted()) {
            initCameraFunction()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 999)
        }
    }

    private fun allPermissionGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initCameraFunction() {
        // ProcessCameraProvider 用於將相機的生命週期綁定到應用的生命週期
        ProcessCameraProvider.getInstance(this).also {
            // 新增 Listener 將動作返回到主線程運行
            it.addListener({
                // 初始化預覽，此設定才會正常顯示相機顯示的畫面
                val preview = Preview.Builder()
                        .build()
                        .also { preview ->
                            preview.setSurfaceProvider(findViewById<PreviewView>(R.id.pv_view_finder)!!.surfaceProvider)
                        }

                imageCapture = ImageCapture.Builder()
                        .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .setTargetRotation(this.display!!.rotation)
                        .setTargetResolution(Size(1280, 720))
                        .build()

                try {
                    // 綁定
                    it.get().also { provider ->
                        provider.unbindAll()
                    }.also { provider ->
                        provider.bindToLifecycle(this@CameraXActivity, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "CameraXActivity : Use case binding failed", Toast.LENGTH_SHORT).show()
                }
            }, ContextCompat.getMainExecutor(this@CameraXActivity))
        }
    }

    private fun takePictureFunction() {
        // 設置照片存放位置的File
        val photoFile = File(getOutputDirectory(),
                SimpleDateFormat(FILENAME_FORMAT, Locale.TAIWAN)
                        .format(System.currentTimeMillis()) + ".jpg"
        )

        // 設置輸出方式相關的屬性
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture!!.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(this@CameraXActivity, "Photo capture succeeded: ${Uri.fromFile(photoFile)}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@CameraXActivity, "Photo capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getOutputDirectory(): File {
        // get android.media.packageid.appname.imageName
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }



}