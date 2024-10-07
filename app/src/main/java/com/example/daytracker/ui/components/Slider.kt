package com.example.daytracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytracker.data.model.Sliders
import com.example.daytracker.ui.theme.Typography

@Composable
fun Slider(slider: List<Sliders>) {
    var currentSlider by remember { mutableIntStateOf(0) }
    val current = slider[currentSlider]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = current.title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = current.description,
            style = Typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = current.image,
            contentDescription = "Slider illustration",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (currentSlider < slider.size - 1) {
                    currentSlider++
                } else {
                    current.onDismiss()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Next")
        }
        Spacer(modifier = Modifier.height(16.dp))
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