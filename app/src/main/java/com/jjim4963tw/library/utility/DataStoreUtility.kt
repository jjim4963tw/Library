package com.jjim4963tw.library.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

object DataStoreUtility {
    object PreferencesKey {
        const val STORE_NAME_LIBRARY = "library"

        val KEY_IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferencesKey.STORE_NAME_LIBRARY)

    /**
     * 欲載入 DataStore 中的所有值
     * @param dataStore - DataStore 的實體
     */
    suspend fun loadDataStore(dataStore: DataStore<Preferences>): Preferences {
        return runBlocking {
            dataStore.data.first()
        }
    }
    /**
     * 讀取 DataStore 中的參數
     * @param dataStore - DataStore 的實體
     * @param key - 欲讀取的參數的 Key
     */
    suspend fun<T> getDataStoreValue(dataStore: DataStore<Preferences>, key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[key]
        }
    }
    /**
     * 將參數寫入 DataStore 中
     * @param dataStore - DataStore 的實體
     * @param key - 欲寫入的參數對應的 Key
     * @param value - 欲寫入的值
     */
    suspend fun<T> setDataStoreValue(dataStore: DataStore<Preferences>, key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            val originValue = it[key]

            it[key] = value
        }
    }

    /**
     * 清除 DataStore 中所有資料
     * @param dataStore - DataStore 的實體
     */
    suspend fun clearDataStore(dataStore: DataStore<Preferences>) {
        dataStore.edit {
            it.clear()
        }
    }
}