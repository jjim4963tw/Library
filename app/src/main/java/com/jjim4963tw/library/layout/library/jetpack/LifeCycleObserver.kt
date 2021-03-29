package com.jjim4963tw.library.layout.library.jetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifeCycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectionListener() {
        Log.e("LifeCycleObserver","==ON_RESUME==")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectionListener() {
        Log.e("LifeCycleObserver","==ON_PAUSE==")
    }
}