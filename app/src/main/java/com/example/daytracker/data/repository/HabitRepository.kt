package com.example.daytracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.daytracker.data.model.Habit
import com.example.daytracker.domain.utils.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString

class HabitRepository(private val context: Context) {
    private val dataStore = context.dataStore
    private val habitsKey = stringPreferencesKey("habits")

    suspend fun saveHabits(habits: List<Habit>) {
        val habitsJson = json.encodeToString(habits)
        dataStore.edit { preferences ->
            preferences[habitsKey] = habitsJson
        }
    }

    private fun getHabits(): Flow<List<Habit>> {
        return dataStore.data.map { preferences ->
            val habitsJSON = preferences[habitsKey] ?: "[]"
            json.decodeFromString(habitsJSON)
        }
    }

    fun getHabitsBlocking(): List<Habit> {
        return runBlocking {
            getHabits().first()
        }
    }
}