package com.example.daytracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.ui.viewmodel.TasksViewModel
import java.util.Calendar

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
                // Asegúrate de inicializar completedDate si es necesario
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
                    onTaskDelete = taskViewModel::deleteTask,
                    getCompletedTasksPerDay = taskViewModel::getCompletedTasksPerDay
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
    onTaskDelete: (Tasks) -> Unit,
    getCompletedTasksPerDay: () -> List<Pair<String, Int>>
) {
    var expanded by remember { mutableStateOf(false) }
    var editable by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(task.title) }
    var newDescription by remember { mutableStateOf(task.description) }
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(0f to 0, 300f to 1) // Ajusta el valor 300f según sea necesario

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
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                val updatedTask = task.copy(title = newTitle, description = newDescription)
                                onTaskUpdate(updatedTask)
                                editable = false
                            }) {
                                Text("Guardar")
                            }
                            Button(onClick = { editable = false }) {
                                Text("Cancelar")
                            }
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { editable = true }) {
                                Text("Editar")
                            }
                            Button(onClick = { onTaskDelete(task) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Swipe,
                        contentDescription = "Deslizar para ver progreso",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        if (swipeableState.currentValue == 1) {
            // Mostrar el gráfico cuando se deslice
            TaskProgressGraph(
                completedTasksPerDay = getCompletedTasksPerDay(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun TaskProgressGraph(
    completedTasksPerDay: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    // Dimensiones del gráfico
    val graphHeight = 200f
    val graphWidth = 300f

    Canvas(
        modifier = modifier
    ) {
        val padding = 40f
        val width = size.width - padding * 2
        val height = size.height - padding * 2

        // Filtrar los últimos 30 días si es necesario
        val data = completedTasksPerDay.takeLast(30)

        if (data.isEmpty()) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "No hay datos para mostrar",
                    size.width / 2,
                    size.height / 2,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 40f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }
            return@Canvas
        }

        // Encontrar el valor máximo para escalar el gráfico
        val maxY = data.maxOfOrNull { it.second }?.toFloat() ?: 1f

        // Dibujar ejes
        drawLine(
            color = Color.Black,
            start = Offset(padding, padding),
            end = Offset(padding, size.height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = Offset(padding, size.height - padding),
            end = Offset(size.width - padding, size.height - padding),
            strokeWidth = 2f
        )

        // Calcular los puntos del gráfico
        val points = data.mapIndexed { index, pair ->
            val x = padding + (width / (data.size - 1)) * index
            val y = size.height - padding - (pair.second / maxY) * height
            Offset(x, y)
        }

        // Dibujar líneas del gráfico
        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFF0D47A1),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3f
            )
        }

        // Dibujar puntos
        points.forEach { point ->
            drawCircle(
                color = Color.Red,
                radius = 4f,
                center = point
            )
        }

        // Dibujar etiquetas del eje Y
        val paintY = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 30f
            textAlign = android.graphics.Paint.Align.RIGHT
            isAntiAlias = true
        }

        val numberOfYAxisLabels = 5
        for (i in 0..numberOfYAxisLabels) {
            val label = ((maxY / numberOfYAxisLabels) * i).toInt().toString()
            val y = size.height - padding - (i / numberOfYAxisLabels.toFloat()) * height
            drawContext.canvas.nativeCanvas.drawText(
                label,
                padding - 10,
                y + 10,
                paintY
            )
        }

        // Dibujar etiquetas del eje X (últimos 7 días por simplicidad)
        val paintX = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        val daysToShow = 7
        val step = if (data.size < daysToShow) 1 else data.size / daysToShow
        for (i in 0 until daysToShow) {
            val index = i * step
            if (index < data.size) {
                val x = padding + (width / (data.size - 1)) * index
                val day = data[index].first
                drawContext.canvas.nativeCanvas.drawText(
                    day,
                    x,
                    size.height - padding + 30,
                    paintX
                )
            }
        }
    }
}

fun generateSampleData(): List<Pair<String, Int>> {
    // Genera datos de muestra para los últimos 30 días
    val data = mutableListOf<Pair<String, Int>>()
    val calendar = Calendar.getInstance()
    for (i in 29 downTo 0) {
        val day = calendar.clone() as Calendar
        day.add(Calendar.DAY_OF_MONTH, -i)
        val dayStr = "${day.get(Calendar.DAY_OF_MONTH)}/${day.get(Calendar.MONTH) + 1}"
        // Simula la cantidad de tareas completadas (0 a 5)
        val tasksCompleted = (0..5).random()
        data.add(Pair(dayStr, tasksCompleted))
    }
    return data
}
