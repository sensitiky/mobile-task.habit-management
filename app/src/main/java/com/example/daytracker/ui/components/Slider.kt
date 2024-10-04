package com.example.daytracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daytracker.data.model.Sliders
import com.example.daytracker.ui.theme.Typography

@Composable
fun Slider(slider: List<Sliders>) {
    var currentSlider by remember { mutableStateOf(0) }
    val current = slider[currentSlider]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = current.title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = current.description, style = Typography.bodyMedium)
        Image(painter = current.image, contentDescription = "Slider illustration")
        Button(onClick = {
            if (currentSlider < slider.size - 1) {
                currentSlider++
            } else {
                current.onDismiss()
            }
        }) {
            Text("Next")
        }
        SliderDots(currentSlider, slider.size)
    }
}

@Composable
fun SliderDots(currentSlider: Int, totalSliders: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        repeat(totalSliders) { index ->
            Icon(
                imageVector = Icons.Default.Brightness1,
                contentDescription = "Slider dot",
                tint = if (index == currentSlider) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.4f
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}