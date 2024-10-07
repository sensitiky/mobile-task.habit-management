package com.example.daytracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.daytracker.R
import com.example.daytracker.data.model.Sliders
import com.example.daytracker.ui.components.Login
import com.example.daytracker.ui.components.Register
import com.example.daytracker.ui.components.Slider

@Composable
fun WelcomeScreen(
    onLoginDismiss: (String, String) -> Unit,
    onRegisterDismiss: (String, String, String) -> Unit
) {
    var showLogin by remember { mutableStateOf(false) }
    var showRegister by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                showLogin -> {
                    Login(
                        onLogin = onLoginDismiss,
                        onRegisterClick = { showRegister = true; showLogin = false }
                    )
                }

                showRegister -> {
                    Register(onRegister = onRegisterDismiss,
                        onLoginClick = { showLogin = true; showRegister = false })
                }

                else -> {
                    Slider(
                        slider = listOf(
                            Sliders(
                                title = "Welcome to TaskHand",
                                description = "Every task & project in one place",
                                image = painterResource(id = R.drawable.taskhand),
                                onDismiss = { showLogin = true }
                            ),
                            Sliders(
                                title = "Improved productivity",
                                description = "Get more done with less effort",
                                image = painterResource(id = R.drawable.taskhand),
                                onDismiss = { showLogin = true }
                            )
                        )
                    )
                }
            }
        }
    }
}