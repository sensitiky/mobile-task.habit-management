package com.example.daytracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.daytracker.data.model.Habit
import com.example.daytracker.ui.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Home(imagePainter: Painter, habitViewModel: HabitViewModel) {
    val habits = remember { habitViewModel.mutableHabits }
    HomeScreen(
        imagePainter = imagePainter,
        title = "Track Your Life",
        subtitle = "Daily Tasks",
        habits = habits,
        onHabitUpdate = habitViewModel::updateHabit
    )
}

@Composable
fun HomeScreen(
    imagePainter: Painter,
    title: String,
    subtitle: String,
    habits: List<Habit>,
    onHabitUpdate: (Habit) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            modifier = Modifier
                .width(300.dp)
                .height(200.dp)
                .padding(top = 20.dp),
            painter = imagePainter,
            contentDescription = "Tracker Logo"
        )
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Text(text = subtitle, style = MaterialTheme.typography.bodyLarge)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits) { habit ->
                HabitCard(habit, onHabitUpdate = onHabitUpdate)
            }
        }
    }
}


@Composable
fun HabitCard(habit: Habit, onHabitUpdate: (Habit) -> Unit) {
    val editable = remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(habit.title) }
    val formatter = SimpleDateFormat("dd/MM - E/MM", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (editable.value) {
                    TextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") }
                    )
                } else {
                    Text(text = habit.title, style = MaterialTheme.typography.bodyLarge)
                }
                Text(text = habit.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatter.format(habit.createdAt),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatter.format(habit.updatedAt),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { editable.value = !editable.value }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = if (editable.value) "Cancel" else "Edit"
                    )
                }
                if (editable.value) {
                    IconButton(onClick = {
                        val updateHabit = habit.copy(title = newTitle)
                        onHabitUpdate(updateHabit)
                        editable.value = false
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save changes"
                        )
                    }
                }
            }
        }
    }
}