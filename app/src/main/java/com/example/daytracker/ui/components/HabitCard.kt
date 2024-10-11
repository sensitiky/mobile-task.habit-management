package com.example.daytracker.ui.components

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.daytracker.data.model.Habit
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HabitCard(habit: Habit, onHabitUpdate: (Habit) -> Unit, onHabitDelete: (Habit) -> Unit) {
    val editable = remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(habit.title) }
    var newDescription by remember { mutableStateOf(habit.description) }
    var newDate by remember { mutableStateOf(habit.updatedAt) }
    val formatter = SimpleDateFormat("dd/MM - E/MM", Locale.getDefault())
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { timeInMillis = newDate.time }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
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
                        label = { Text("Title") },
                        colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.onSurface),
                    )
                    TextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Description") },
                        colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.onSurface),
                    )
                    Button(onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                                val newCalendar = Calendar.getInstance()
                                newCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                                newDate = Timestamp(calendar.timeInMillis)
                            },
                            year,
                            month,
                            day
                        )
                        datePickerDialog.show()
                    }) {
                        Text("Select Date")
                    }
                } else {
                    Text(text = habit.title, style = MaterialTheme.typography.bodyLarge)
                    Text(text = habit.description, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = formatter.format(habit.createdAt),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatter.format(newDate),
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
                AnimatedVisibility(visible = editable.value) {
                    IconButton(onClick = {
                        val updateHabit = habit.copy(
                            title = newTitle,
                            description = newDescription,
                            updatedAt = newDate
                        )
                        onHabitUpdate(updateHabit)
                        editable.value = false
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save changes"
                        )
                    }
                }
                IconButton(onClick = { onHabitDelete(habit) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Habit"
                    )
                }
            }
        }
    }
}