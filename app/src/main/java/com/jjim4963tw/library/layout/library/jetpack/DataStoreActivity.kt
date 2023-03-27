package com.jjim4963tw.library.layout.library.jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.lifecycle.lifecycleScope
import com.jjim4963tw.library.R
import com.jjim4963tw.library.utility.DataStoreUtility
import com.jjim4963tw.library.utility.DataStoreUtility.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException


class DataStoreActivity : AppCompatActivity() {
    enum class UIMode {
        LIGHT, DARK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_store)

        lifecycleScope.launch {
            // 欲載入所有參數
            DataStoreUtility.loadDataStore(dataStore)

            // 取得特定 Key 對應的值
            DataStoreUtility.getDataStoreValue(dataStore, DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE).collect {
                it?.let {
                    Log.e("is_Dark_mode", if (it) UIMode.LIGHT.toString() else UIMode.LIGHT.toString())
                }
            }
        }

        runBlocking {
            // 設定值至 DataStore
            DataStoreUtility.setDataStoreValue(dataStore, DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE, true)
        }
    }
}