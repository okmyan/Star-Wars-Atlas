package com.okmyan.starwarsatlas.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "last_refresh")

@Singleton
class LastRefreshDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun getTimestamp(key: String): Long? =
        context.dataStore.data.first()[longPreferencesKey(key)]

    suspend fun setTimestamp(key: String, timestamp: Long) {
        context.dataStore.edit { it[longPreferencesKey(key)] = timestamp }
    }

}
