package com.jjim4963tw.library.utility

import android.content.Context
import android.os.Bundle
import android.util.ArrayMap
import androidx.core.os.bundleOf
import java.lang.Exception

object AnalyticsUtility {
    fun sendException(context: Context, title: String, exception: Exception) {
//        val bundle = bundleOf(
//                FirebaseAnalytics.Param.METHOD to "click",
//                "action" to "Exception",
//                "label" to "Exception",
//                "Description" to "$title : ${Thread.currentThread().name}",
//                "Fatal" to false
//        )
//        FirebaseAnalytics.getInstance(context).apply {
//            this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//        }
    }

    fun senEvent(context: Context, arrayMap: ArrayMap<String, String>, event: String) {
//        val bundle = Bundle().apply {
//            arrayMap.keys.forEach {
//                this.putString(it, arrayMap[it])
//            }
//        }
//        FirebaseAnalytics.getInstance(context).apply {
//            this.logEvent(event, bundle)
//        }
    }
}