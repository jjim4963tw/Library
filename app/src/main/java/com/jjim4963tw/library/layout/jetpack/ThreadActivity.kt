package com.jjim4963tw.library.layout.jetpack

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class ThreadActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // common Coroutine
        GlobalScope.launch {
            updateWithDelay()
        }

        // Activity Coroutine
        lifecycleScope.launch {
            // do something
        }
    }

    // 此與Thread.Sleep類似，但並不是停止Thread 5秒，而是讓函式暫時return，5秒後再回來繼續執行
    private suspend fun updateWithDelay() {
        delay(5000)
        // do something
    }

    private fun cancelCoroutineFunction() {
        // Cancel Coroutine
        val job = GlobalScope.launch {
            if (isActive) {
                updateWithDelay()
            }

            try {
                updateWithDelay()
            } finally {
                // release resource
            }
        }
        job.cancel()
    }

    private suspend fun asyncCoroutineFunction() {
        val deferred = GlobalScope.async {
            delay(500)
            return@async listOf<String>()
        }
        val list = deferred.await()
    }

    private suspend fun withContextFunction() {
        val list = withContext(Dispatchers.IO) {
            delay(500)
            return@withContext listOf<String>()
        }
    }

    private suspend fun coroutineScopeFunction() {
        val list = coroutineScope {
            delay(500)
            return@coroutineScope listOf<String>()
        }
    }

    private fun getList(): List<String> = runBlocking {
        delay(500)
        return@runBlocking listOf<String>()
    }

    private fun doFlow() = runBlocking {
        demoFlow().collect {
            // 每五秒依序收到1~3
            println(it)
        }
    }

    private fun demoFlow(): Flow<Int> = flow {
        for (i in 1 .. 3) {
            delay(500)
            emit(i)
        }
    }
}

class ThreadFragment: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment Coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            // do something
        }
    }
}

class ThreadViewModel: ViewModel() {
    init {
        viewModelScope.launch {
            // do something
        }
    }
}