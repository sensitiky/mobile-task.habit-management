package com.example.daytracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.ui.components.DefaultSearchBar
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    taskViewModel: TasksViewModel,
    viewModel: ContextViewModel
) {
    val tasks by taskViewModel.task.observeAsState(emptyList())
    val tasksCounter = tasks.size
    val completedTasks = tasks.count { it.completed }
    val query = remember { mutableStateOf("") }
    val filteredTasks = tasks.filter {
        it.title.contains(query.value, ignoreCase = true) || it.description.contains(
            query.value,
            ignoreCase = true
        )
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tasks", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    MaterialTheme.colorScheme.primaryContainer,
                    Color.White,
                    Color.White,
                    Color.White,
                    Color.White
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newTask = Tasks(
                        id = tasks.size + 1,
                        title = "New Task " + (tasksCounter + 1),
                        description = "Description of the new task",
                        completed = false
                    )
                    taskViewModel.createTask(newTask)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileHeader(completedTasks, tasks.size, viewModel)
            DefaultSearchBar(viewModel, onQueryChange = { query.value = it })
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskCard(
                        task = task,
                        onTaskUpdate = taskViewModel::updateTask,
                        onTaskDelete = taskViewModel::deleteTask,
                        onTaskComplete = taskViewModel::completedTask,
                        onTaskUncompleted = taskViewModel::unCompletedTask
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(completedTasks: Int, totalTasks: Int, viewModel: ContextViewModel) {
    val user by viewModel.user.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                AsyncImage(
                    model = user.avatar,
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = user.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Task Progress",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LinearProgressIndicator(
                    progress = { completedTasks.toFloat() / totalTasks.coerceAtLeast(1) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
                Text(
                    text = "$completedTasks / $totalTasks completed",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Tasks,
    onTaskUpdate: (Tasks) -> Unit,
    onTaskDelete: (Tasks) -> Unit,
    onTaskComplete: (Tasks) -> Unit,
    onTaskUncompleted: (Tasks) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var editable by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(task.title) }
    var newDescription by remember { mutableStateOf(task.description) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = if (task.completed) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
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
                    text = if (expanded) newTitle else task.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = task.completed,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onTaskComplete(task)
                        } else {
                            onTaskUncompleted(task)
                        }
                    }
                )
            }
            if (expanded) {
                Text(
                    text = newDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (editable) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()
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
                                val updatedTask =
                                    task.copy(title = newTitle, description = newDescription)
                                onTaskUpdate(updatedTask)
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
                            onClick = { onTaskDelete(task) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
