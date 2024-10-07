package com.example.daytracker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytracker.data.model.User
import com.example.daytracker.data.repository.ContextRepository
import com.example.daytracker.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Response

class ContextViewModel(
    private val userRepository: UserRepository,
    private val contextRepository: ContextRepository
) : ViewModel() {

    //Expose the user state flow
    val user: StateFlow<User> = userRepository.user
        .stateIn(viewModelScope, SharingStarted.Lazily, User())

    //Expose the user logged in state flow
    val isUserLoggedIn: StateFlow<Boolean> = contextRepository.isUserLoggedIn
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response: Response<User> = userRepository.login(username, password)
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("ContextViewModel", "Login exitoso para: ${user.name}")
                        // Update the session state
                        contextRepository.setSessionActive(true)
                        // Changes in isUserLoggedIn will be automatically reflected
                    } else {
                        Log.e("ContextViewModel", "Respuesta de usuario es nula")
                        // Handle the case where the response body is null
                    }
                } else {
                    Log.e(
                        "ContextViewModel",
                        "Error de login: ${response.code()} - ${response.message()}"
                    )
                    // Handle response errors, such as invalid credentials
                }
            } catch (e: Exception) {
                Log.e("ContextViewModel", "Excepción durante el login", e)
                // Handle exceptions, such as network problems
            }
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response: Response<User> = userRepository.register(name, email, password)
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("ContextViewModel", "Registro exitoso para: ${user.name}")
                        // Update the session state
                        contextRepository.setSessionActive(true)
                        // Changes in isUserLoggedIn will be automatically reflected
                    } else {
                        Log.e("ContextViewModel", "Respuesta de usuario es nula")
                        // Handle the case where the response body is null
                    }
                } else {
                    Log.e(
                        "ContextViewModel",
                        "Error de registro: ${response.code()} - ${response.message()}"
                    )
                    // Handle response errors, such as email already in use
                }
            } catch (e: Exception) {
                Log.e("ContextViewModel", "Excepción durante el registro", e)
                // Handle exceptions, such as network problems
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            contextRepository.setSessionActive(false)
            Log.d("ContextViewModel", "Usuario deslogueado")
            // Optionally, clear the user data
        }
    }
}