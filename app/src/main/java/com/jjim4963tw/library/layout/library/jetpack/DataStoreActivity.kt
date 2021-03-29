package com.jjim4963tw.library.layout.library.jetpack

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import androidx.lifecycle.lifecycleScope
import com.jjim4963tw.library.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException


class DataStoreActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_store)

        lifecycleScope.launch {
            dataStore.data.first()
        }

        runBlocking {
            modifyStringValue()
            modifyIntValue()
        }
    }

    private suspend fun modifyStringValue() {
        // set key and value type
        val KEY_USERID = stringPreferencesKey("user_id")

        // read value for key
        dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map {
                    it[KEY_USERID] ?: ""
                }

        dataStore.edit { setting ->
            // get DataStore value
            val userID = setting[KEY_USERID] ?: ""

            // set DataStore value
            setting[KEY_USERID] = "testAccount"
        }
    }

    private suspend fun modifyIntValue() {
        // set key and value type
        val KEY_YEARS = intPreferencesKey("years")

        // read value for key
        dataStore.data.map {
            it[KEY_YEARS] ?: 0
        }

        dataStore.edit { setting ->
            // get DataStore value
            val userYears = setting[KEY_YEARS] ?: 0

            // set DataStore value
            setting[KEY_YEARS] = 30
        }
    }

    enum class UIMode {
        LIGHT, DARK
    }

    private suspend fun usedFlowDataStore() {
        val KEY_DARK_MODE = booleanPreferencesKey("is_dark_mode")

        val uiModeFlow: Flow<UIMode> = dataStore.data
                .catch {
                    if (it is IOException) {
                        it.printStackTrace()
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }.map {
                    when (it[KEY_DARK_MODE] ?: false) {
                        true -> UIMode.DARK
                        false -> UIMode.LIGHT
                    }
                }
    }

}