package com.jjim4963tw.library

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jjim4963tw.library.utility.DataStoreUtility
import com.jjim4963tw.library.utility.DataStoreUtility.dataStore
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DataStoreUnitTest {
    private lateinit var datastore: DataStore<Preferences>

    @Before
    fun setUp() {
        datastore = InstrumentationRegistry.getInstrumentation().targetContext.dataStore
    }

    /**
     * 測試 DataStore 新增與取得參數是否正確
     */
    @Test
    fun testInsertAndGet(): Unit = runBlocking {
        datastore.edit { preferences ->
            preferences[DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE] = true
        }
        val result = datastore.data.first()[DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE]

        assertEquals(true, result)
    }

    /**
     * 測試刪除參數是否正確
     */
    @Test
    fun testDelete() = runBlocking {
        datastore.edit { preferences ->
            preferences[DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE] = true
        }

        datastore.edit { preferences ->
            preferences.remove(DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE)
        }

        val result = datastore.data.first()[DataStoreUtility.PreferencesKey.KEY_IS_DARK_MODE]

        assertEquals(null, result)
    }
}