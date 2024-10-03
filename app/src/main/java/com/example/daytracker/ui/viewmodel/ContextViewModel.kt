package com.example.daytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytracker.data.model.User
import com.example.daytracker.data.repository.ContextRepository
import com.example.daytracker.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContextViewModel(
    private val userRepository: UserRepository,
    private val contextRepository: ContextRepository
) : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> get() = _user

    val isUserLoggedIn = contextRepository.isUserLoggedIn

    init {
        viewModelScope.launch {
            userRepository.user.collect { userData ->
                _user.value = userData
            }
        }
    }
    fun loginUser(){
        viewModelScope.launch {
            contextRepository.setSessionActive(true)
        }
    }
    fun logoutUser() {
        viewModelScope.launch {
            contextRepository.setSessionActive(false)
            _user.value = User()
        }
    }
}