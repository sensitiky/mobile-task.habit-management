package com.example.daytracker.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytracker.data.network.UserApi
import com.example.daytracker.data.repository.ContextRepository
import com.example.daytracker.data.repository.UserRepository
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.ContextViewModelFactory

@Composable
fun AppScreen() {
    val context = LocalContext.current
    val userApi = UserApi.create("https://api.example.com")
    val userRepository = UserRepository(userApi, context)
    val contextRepository = ContextRepository(context)
    val viewModel: ContextViewModel =
        viewModel(factory = ContextViewModelFactory(userRepository, contextRepository))
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState(initial = false)
    if (isUserLoggedIn) {
        MainScreen(viewModel)
    } else {
        WelcomeScreen(onDismiss = {
            viewModel.loginUser()
        })
    }

}