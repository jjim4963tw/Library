package com.jjim4963tw.library

import android.app.AlertDialog
import android.content.Context
import kotlinx.coroutines.*

abstract class BaseAsyncTask<Input, Output>(private val context: Context, private var usedDialog: Boolean = false) {
    private lateinit var progressDialog: AlertDialog

    private lateinit var job: Job
    var result: Output? = null

    open fun onPreExecute() {
        if (usedDialog) {
            initProgressDialog()
            progressDialog.show()
        }
    }

    open fun onPostExecute(result: Output?) {
        if (usedDialog && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    abstract fun doInBackground(vararg params: Input): Output?

    open suspend fun cancel() {
        job.cancelAndJoin()
    }

//    fun <T> execute(vararg input: Input) {
//        job = GlobalScope.launch(Dispatchers.Main) {
////            while (isActive) {
//                onPreExecute()
//                callAsync(*input)
////            }
//        }
//    }

    fun execute(vararg input: Input) {
        job = GlobalScope.launch(Dispatchers.Main) {
//            while (isActive) {
            onPreExecute()
            callAsync(*input)
//            }
        }
    }


    private suspend fun callAsync(vararg input: Input) {
        withContext(Dispatchers.IO) {
            result = doInBackground(*input)
        }
        GlobalScope.launch(Dispatchers.Main) {
            onPostExecute(result)
        }
    }

    private fun initProgressDialog() {
        progressDialog = AlertDialog.Builder(context)
            .setView(R.layout.progressbar_vertical_layout)
            .setCancelable(false).create()
    }
}