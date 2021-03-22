package com.jjim4963tw.library.layout.jetpack

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityDataBindingBinding

class LiveDataAndDataBindingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDataBindingBinding

    private val viewMode by viewModels<LiveDataAndDataBindingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding)
//        binding.viewModel = LiveDataAndDataBindingViewModel()
//        binding.setVariable(BR.model, LiveDataAndDataBindingViewModel())

        // first method
        viewMode.names.observe(this) {
            print(it)
        }

        // lifecycle + viewModel + data binding
        val model = ViewModelProvider(this).get(LiveDataAndDataBindingViewModel::class.java)
        binding.viewModel = model
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }


    val textLiveData = MutableLiveData<String>()
    val lengthLiveData: LiveData<Int> = Transformations.map(textLiveData) {
        it.length
    }

//    val listLiveData: LiveData<Int> = Transformations.switchMap(textLiveData) {
//        fetch(it)
//    }
//    fun fetch(query: String): LiveData<List<String>> {
//        repository.load(query)
//    }

}