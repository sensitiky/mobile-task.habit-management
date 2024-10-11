package com.example.daytracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytracker.data.model.Habit
import java.sql.Timestamp

@Composable
fun HabitCard(
    habit: Habit,
    onHabitUpdate: (Habit) -> Unit,
    onHabitDelete: (Habit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var editable by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(habit.title) }
    var newDescription by remember { mutableStateOf(habit.description) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = if (habit.complete) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) newTitle else habit.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = habit.complete,
                    onCheckedChange = { isChecked ->
                        onHabitUpdate(habit.copy(complete = isChecked))
                    }
                )
            }
            if (expanded) {
                Text(
                    text = habit.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (editable) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val updatedHabit = habit.copy(
                                    title = newTitle,
                                    description = newDescription,
                                    updatedAt = Timestamp(System.currentTimeMillis())
                                )
                                onHabitUpdate(updatedHabit)
                                editable = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save")
                        }
                        OutlinedButton(
                            onClick = { editable = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { editable = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Edit")
                        }
                        OutlinedButton(
                            onClick = { onHabitDelete(habit) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminate")
                        }
                    }
                }
            }
        }
    }
}