package com.example.daytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.daytracker.data.repository.ContextRepository
import com.example.daytracker.data.repository.UserRepository

class ContextViewModelFactory(
    private val userRepository: UserRepository,
    private val contextRepository: ContextRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContextViewModel::class.java)) {
            return ContextViewModel(userRepository, contextRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}