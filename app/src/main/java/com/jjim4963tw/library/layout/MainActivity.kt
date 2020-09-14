package com.jjim4963tw.library.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jjim4963tw.library.R
import com.jjim4963tw.library.layout.jetpack.JetPackMainActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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