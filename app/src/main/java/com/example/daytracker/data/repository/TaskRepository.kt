package com.example.daytracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.domain.utils.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString

class TaskRepository(context: Context) {
    private val dataStore = context.dataStore
    private val tasksKey = stringPreferencesKey("tasks")

    suspend fun saveTasks(habits: List<Tasks>) {
        val habitsJson = json.encodeToString(habits)
        dataStore.edit { preferences ->
            preferences[tasksKey] = habitsJson
        }
    }

    private fun getTasks(): Flow<List<Tasks>> {
        return dataStore.data.map { preferences ->
            val habitsJSON = preferences[tasksKey] ?: "[]"
            json.decodeFromString(habitsJSON)
        }
    }

    fun getTasksBlocking(): List<Tasks> {
        return runBlocking {
            getTasks().first()
        }
    }
}