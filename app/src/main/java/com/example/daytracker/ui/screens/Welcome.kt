package com.example.daytracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.daytracker.R
import com.example.daytracker.data.model.Sliders
import com.example.daytracker.ui.components.Slider

@Composable
fun WelcomeScreen(onDismiss: () -> Unit) {
    Scaffold { paddingValues ->
        WelcomeSlider(
            onDismiss, modifier = Modifier
                .padding(paddingValues)
        )
    }
}

@Composable
fun WelcomeSlider(onDismiss: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            slider = listOf(
                Sliders(
                    title = "Welcome to TaskHand",
                    description = "Every task & project in one place",
                    image = painterResource(id = R.drawable.taskhand),
                    onDismiss = onDismiss,
                    currentSlider = 0
                ),
                Sliders(
                    title = "Improved productivity",
                    description = "Get more done with less effort",
                    image = painterResource(id = R.drawable.taskhand),
                    onDismiss = onDismiss,
                    currentSlider = 1
                )
            )
        )
    }
}