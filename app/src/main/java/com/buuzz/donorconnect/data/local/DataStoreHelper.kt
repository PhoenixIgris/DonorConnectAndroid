package com.buuzz.donorconnect.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.buuzz.donorconnect.utils.helpers.AppLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val DATA_STORE_NAME = "DONOR CONNECT DATASTORE"
private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

class DataStoreHelper @Inject constructor(
    @ApplicationContext
    private val appContext: Context
) {

    private val dataStore = appContext.dataStore


    private suspend fun <T> editStore(key: Preferences.Key<T>, value: T) {
        dataStore.edit { store ->
            store[key] = value
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }


    suspend fun saveStringToDatastore(data: Pair<String, String>) =
        withContext(Dispatchers.IO) {
            AppLogger.logD("DataStoreHelper", "saveStringToDatastore: $data")
            val prefKey = stringPreferencesKey(data.first)
            dataStore.edit {
                it[prefKey] = data.second
            }
        }

    suspend fun readStringFromDatastore(key: String): String =
        withContext(Dispatchers.IO) {
            val datastoreKey = stringPreferencesKey(key)
            val value = dataStore.data.first()[datastoreKey]
            AppLogger.logD("DataStoreHelper", "readStringFromDatastore: $key -> $value")
            return@withContext value ?: ""
        }

    suspend fun saveBooleanToDatastore(data: Pair<String, Boolean>) =
        withContext(Dispatchers.IO) {
            AppLogger.logD("DataStoreHelper", "saveBooleanToDatastore: $data")
            val prefKey = booleanPreferencesKey(data.first)
            dataStore.edit {
                it[prefKey] = data.second
            }
        }

    suspend fun readBooleanFromDatastore(key: String): Boolean =
        withContext(Dispatchers.IO) {
            val datastoreKey = booleanPreferencesKey(key)
            val value = dataStore.data.first()[datastoreKey] ?: false
            AppLogger.logD("DataStoreHelper", "readBooleanFromDatastore: $key -> $value")
            return@withContext value
        }

}