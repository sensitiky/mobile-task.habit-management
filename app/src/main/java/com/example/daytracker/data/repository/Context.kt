package com.example.daytracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class ContextRepository(context: Context) {
    private val sessionKey = booleanPreferencesKey("session")
    private val dataStore = context.dataStore

    val isUserLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[sessionKey] ?: false
        }

    suspend fun setSessionActive(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[sessionKey] = isActive
        }
    }
}
