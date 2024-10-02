package com.example.daytracker.ui.viewmodel

import ContextRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContextViewModel(context: Context) : ViewModel() {
    private val repository = ContextRepository(context)
    val isUserLoggedIn: Flow<Boolean> = repository.isUserLoggedIn
    fun loginUser() {
        viewModelScope.launch {
            repository.setSessionActive(true)
        }
    }

    fun logoutUser() {
       viewModelScope.launch {
           repository.setSessionActive(false)
       }
    }
}

class ContextViewModelFactory(private val context: Context):ViewModelProvider.Factory{
    override fun <T:ViewModel>create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(ContextViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ContextViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}