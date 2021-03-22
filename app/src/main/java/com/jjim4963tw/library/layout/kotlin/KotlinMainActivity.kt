package com.jjim4963tw.library.layout.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jjim4963tw.kotlinapp.ClassAndObjectUtil
import com.jjim4963tw.library.R

class KotlinMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val classAndObjectUtil = ClassAndObjectUtil(-1, "message")
//        startActivity(Intent(this, MainActivity::class.java))
    }
}
