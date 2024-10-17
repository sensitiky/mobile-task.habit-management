package com.example.daytracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.daytracker.data.model.User
import com.example.daytracker.data.network.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class UserRepository(private val userApi: UserApi, context: Context) {
    private val userNameKey = stringPreferencesKey("user_name")
    private val userEmailKey = stringPreferencesKey("user_email")
    private val userAvatarKey = stringPreferencesKey("user_avatar")
    private val dataStore = context.dataStore

    suspend fun login(email: String, password: String): Response<User> {
        val response = userApi.login(email, password)

        if (response.isSuccessful) {
            response.body()?.let { user ->
                dataStore.edit { preferences ->
                    preferences[userNameKey] = user.name
                    preferences[userEmailKey] = user.email
                    preferences[userAvatarKey] = user.avatar
                }
            }
        }
        return response
    }

    suspend fun register(username: String, email: String, password: String) =
        userApi.register(username, email, password)

    suspend fun getUser() = userApi.getUser()

    val user: Flow<User> = dataStore.data.map { preferences ->
        User(
            name = preferences[userNameKey] ?: "",
            email = preferences[userEmailKey] ?: "",
            avatar = preferences[userAvatarKey] ?: ""
        )
    }
}