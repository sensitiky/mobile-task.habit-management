package com.example.daytracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(
    taskViewModel: TasksViewModel,
) {
    val tasks by taskViewModel.task.observeAsState(emptyList())
    val completedTasks = tasks.count { it.completed }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tasks Overview",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Completed Tasks", fontSize = 20.sp)
                Text(
                    text = "$completedTasks / ${tasks.size}",
                    fontSize = 24.sp,
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(onClick = {
            val newTask = Tasks(
                id = tasks.size + 1,
                title = "New Task",
                description = "Description of the new task",
                completed = false
            )
            taskViewModel.createTask(newTask)
        }) {
            Text("Add Task")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onTaskUpdate = taskViewModel::updateTask,
                    onTaskDelete = taskViewModel::deleteTask
                )
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun TaskCard(
    task: Tasks,
    onTaskUpdate: (Tasks) -> Unit,
    onTaskDelete: (Tasks) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var editable by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(task.title) }
    var newDescription by remember { mutableStateOf(task.description) }
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(0f to 0, 300f to 1) // Adjust the value 300f as necessary

    Box {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { expanded = !expanded }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (task.completed) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = task.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                if (expanded) {
                    Text(text = task.description, fontSize = 16.sp, color = Color.Gray)
                    if (editable) {
                        TextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                val updatedTask =
                                    task.copy(title = newTitle, description = newDescription)
                                onTaskUpdate(updatedTask)
                                editable = false
                            }) {
                                Text("Save")
                            }
                            Button(onClick = { editable = false }) {
                                Text("Cancel")
                            }
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { editable = true }) {
                                Text("Edit")
                            }
                            Button(onClick = { onTaskDelete(task) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}