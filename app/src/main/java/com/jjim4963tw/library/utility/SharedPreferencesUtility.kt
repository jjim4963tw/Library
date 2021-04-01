package com.jjim4963tw.library.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

object SharedPreferencesUtility {
    private fun getPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getIntValue(context: Context, key: String): Int {
        return getPreferences(context).getInt(key, -1)
    }

    fun getStringValue(context: Context, key: String): String? {
        return getPreferences(context).getString(key, null)
    }

    fun getBooleanValue(context: Context, key: String): Boolean {
        return getPreferences(context).getBoolean(key, false)
    }

    fun setValue(context: Context, map: HashMap<String, Any>) {
         getPreferences(context).edit(commit = true) {
            map.keys.forEach {
                when {
                    map[it] is String -> {
                        this.putString(it, map[it] as String)
                    }
                    map[it] is Int -> {
                        this.putInt(it, map[it] as Int)
                    }
                    map[it] is Long -> {
                        this.putLong(it, map[it] as Long)
                    }
                    map[it] is Float -> {
                        this.putFloat(it, map[it] as Float)
                    }
                    map[it] is Boolean -> {
                        this.putBoolean(it, map[it] as Boolean)
                    }
                    else -> {
                        this.putString(it, map[it] as String)
                    }
                }
            }
        }
    }
}