package com.jjim4963tw.library.layout.jetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityDataBindingBinding

class LiveDataAndDataBindingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDataBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding)
//        binding.viewModel = LiveDataAndDataBindingViewModel()
//        binding.setVariable(BR.model, LiveDataAndDataBindingViewModel())

        val model = ViewModelProvider(this).get(LiveDataAndDataBindingViewModel::class.java)
        binding.viewModel = model
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}