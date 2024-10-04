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

    // Exponer el estado del usuario
    val user: StateFlow<User> = userRepository.user
        .stateIn(viewModelScope, SharingStarted.Lazily, User())

    // Exponer el estado de sesión desde ContextRepository
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
                        // Actualizar el estado de sesión
                        contextRepository.setSessionActive(true)
                        // Los cambios en isUserLoggedIn serán reflejados automáticamente
                    } else {
                        Log.e("ContextViewModel", "Respuesta de usuario es nula")
                        // Manejar el caso donde el cuerpo de la respuesta es nulo
                    }
                } else {
                    Log.e(
                        "ContextViewModel",
                        "Error de login: ${response.code()} - ${response.message()}"
                    )
                    // Manejar errores de la respuesta, como credenciales inválidas
                }
            } catch (e: Exception) {
                Log.e("ContextViewModel", "Excepción durante el login", e)
                // Manejar excepciones, como problemas de red
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            contextRepository.setSessionActive(false)
            Log.d("ContextViewModel", "Usuario deslogueado")
            // Opcional: Limpiar los datos del usuario si es necesario
        }
    }
}