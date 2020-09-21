package com.jjim4963tw.library.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.OnCompleteListener
import com.google.android.play.core.tasks.Task
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityMainBinding
import com.jjim4963tw.library.layout.jetpack.JetPackMainActivity

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_UPDATE = 9001

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkUpdateFunction()
    }

    private fun checkUpdateFunction() {
        AppUpdateManagerFactory.create(this).also { appUpdateManager ->
            appUpdateManager.appUpdateInfo.also { appUpdateInfo ->
                appUpdateInfo.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.apply {
                            if (this.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && this.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                try {
                                    appUpdateManager.startUpdateFlowForResult(this, AppUpdateType.IMMEDIATE, this@MainActivity, REQUEST_CODE_UPDATE)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun openStorageAndMediaPage(view: View) {
        Intent(this, StorageAndMediaActivity::class.java).run {
            startActivity(this)
        }
    }

    fun openJerPackPage(view: View) {
        Intent(this, JetPackMainActivity::class.java).run {
            startActivity(this)
        }
    }
}