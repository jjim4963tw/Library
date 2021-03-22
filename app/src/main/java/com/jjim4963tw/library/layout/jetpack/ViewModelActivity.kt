package com.jjim4963tw.library.layout.jetpack

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelActivity: AppCompatActivity() {

    val model by viewModels<MyViewModel>()
    lateinit var factory: MyViewModel.Factory
    val model_1 by viewModels<MyViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model_2 = ViewModelProvider(this).get(MyViewModel::class.java)
    }
}

class MyViewModel(private val test: String): ViewModel() {
    class Factory(private val test: String): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MyViewModel(test) as T
        }
    }
}