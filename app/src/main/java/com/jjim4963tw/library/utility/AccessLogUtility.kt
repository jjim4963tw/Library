package com.jjim4963tw.library.utility

import android.util.Log
import com.jjim4963tw.library.Library

object AccessLogUtility {
    fun showInfoMessage(onlyDebugModeShow: Boolean, tag: String?, message: String, throwable: Throwable?) {
        if (onlyDebugModeShow && Library.isDebugMode) {
            if (throwable != null) {
                Log.i(tag, message, throwable)
            } else {
                Log.i(tag, message)
            }
        } else {
            Log.d(tag, message, throwable)
        }
    }

    fun showDebugMessage(tag: String?, message: String, throwable: Throwable?) {
        Log.d(tag, message, throwable)
    }

    fun showWarningMessage(onlyDebugModeShow: Boolean, tag: String?, message: String, throwable: Throwable?) {
        if (onlyDebugModeShow && Library.isDebugMode) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        } else {
            Log.d(tag, message, throwable)
        }
    }

    fun showErrorMessage(onlyDebugModeShow: Boolean, tag: String?, message: String, throwable: Throwable?) {
        if (onlyDebugModeShow && Library.isDebugMode) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        } else {
            Log.d(tag, message, throwable)
        }
    }
}