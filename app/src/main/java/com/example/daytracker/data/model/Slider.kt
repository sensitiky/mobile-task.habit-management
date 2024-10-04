package com.example.daytracker.data.model

import androidx.compose.ui.graphics.painter.Painter

data class Sliders(
    val title: String = "",
    val description: String = "",
    val image: Painter,
    val onDismiss: () -> Unit,
    val currentSlider: Int = 0
)