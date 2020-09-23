package com.jjim4963tw.library.layout.jetpack

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.collection.arraySetOf
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.navArgs
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jjim4963tw.library.R
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class JetPackMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_pack)

        lifecycle.addObserver(LifeCycleObserver())

        setKtxCollectionArray()
    }

    fun goLifeCycleActivity(view: View) {
        Intent(this, LifeCycleActivity::class.java).run {
            startActivity(this)
        }
    }

    fun goDataStoreActivity(view: View) {
        Intent(this, DataStoreActivity::class.java).run {
            startActivity(this)
        }
    }

    private fun putKtxSharedPreferencesValueFunction() {
        getSharedPreferences("sharepref", MODE_PRIVATE).edit {
            putBoolean("key", false)
        }

        getSharedPreferences("sharepref", MODE_PRIVATE).edit(commit = true) {
            putString("key", "value")
        }
    }

    private fun setKtxCollectionArray() {
        // result：1, 2, 3, 4, 5, 6
        val combinedArraySet = arraySetOf(1, 2, 3) + arraySetOf(4, 5, 6)
        Log.d("combinedArraySet: ", combinedArraySet.toString())

        // result：1, 2, 3, 4, 5, 6, 7, 8
        val newArraySet = combinedArraySet + 7 + 8
        Log.d("combinedArraySet: ", newArraySet.toString())
    }

    private fun setKtxFragmentFunction() {
        supportFragmentManager.commit {
            addToBackStack("Tag")
            setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit)
            add(Fragment(), "...")
        }
    }

    private fun setKtxLivaData() {
//        val user: LiveData<User> = liveData {
//            val data = database.loadUser() // loadUser is a suspend function.
//            emit(data)
//        }
//
//        val userResult: LiveData<Result> = liveData {
//            emit(Result.loading())
//            try {
//                emit(Result.success(fetchUser()))
//            } catch(ioException: Exception) {
//                emit(Result.error(ioException))
//            }
//        }
    }

    //    val args by navArgs<JetPackMainActivityArgs>()
    private fun setKtxNavigation() {
//        findViewById<Button>(R.id.button3)
//                .setOnClickListener {
//                    // Fragment extension added to retrieve a NavController from
//                    // any destination.
//                    findNavController().navigate(R.id.action_to_next_destination)
//                }

    }

    class MainViewModel : ViewModel() {
        // Make a network request without blocking the UI thread
        private fun makeNetworkRequest() {
            // launch a coroutine in viewModelScope
            viewModelScope.launch {
                // remoteApi.slowFetch()
                // ...
            }
        }
        // No need to override onCleared()
    }

    class CoroutineDownloadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result = coroutineScope {
            val jobs = (0 until 100).map {
                async {
//                    downloadSynchronously("https://www.google.com")
                }
            }
            // awaitAll will throw an exception if a download fails, which
            // CoroutineWorker will treat as a failure
            jobs.awaitAll()
            Result.success()

//            WorkManager.getInstance(context)
//                    .beginWith(longWorkRequest)
//                    .enqueue().await()
        }
    }
}
