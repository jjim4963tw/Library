package com.jjim4963tw.library.layout.jetpack


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LiveDataAndDataBindingViewModel : ViewModel() {
    var names: MutableLiveData<String> = MutableLiveData()
    var ages: MutableLiveData<Int> = MutableLiveData()
    var isMan: MutableLiveData<Boolean> = MutableLiveData()

    init {
        names.postValue("Name")
        ages.postValue(23)
        isMan.postValue(true)
    }

    fun setData() {
        names.postValue("Name1")
        ages.postValue(24)
        isMan.postValue(false)
    }
}