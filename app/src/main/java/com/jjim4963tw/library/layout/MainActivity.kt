package com.jjim4963tw.library.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityMainBinding
import com.jjim4963tw.library.layout.library.jetpack.JetPackMainActivity
import com.jjim4963tw.library.layout.library.DaggerActivity
import com.jjim4963tw.library.layout.library.RetrofitActivity
import com.jjim4963tw.library.layout.library.RxJavaActivity
import com.jjim4963tw.library.layout.media.StorageAndMediaActivity

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_UPDATE = 9001

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkUpdateFunction()

        val spinner = Spinner(this)
        ArrayAdapter(this, R.layout.activity_recycler_view, arrayOf("", "")).apply {
            setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        }.run {
            spinner.adapter = this
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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

    fun openJetpackPage(view: View) {
        Intent(this, JetPackMainActivity::class.java).run {
            startActivity(this)
        }
    }

    fun openRxJavaPage(view: View) {
        Intent(this, RxJavaActivity::class.java).run {
            startActivity(this)
        }
    }

    fun openRetrofitPage(view: View) {
        Intent(this, RetrofitActivity::class.java).run {
            startActivity(this)
        }
    }

    fun openDaggerPage(view: View) {
        Intent(this, DaggerActivity::class.java).run {
            startActivity(this)
        }
    }
}