package com.example.daytracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daytracker.data.model.Habit
import com.example.daytracker.ui.components.HabitCard
import com.example.daytracker.ui.components.SearchBar
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.HabitViewModel
import java.sql.Timestamp

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Home(
    habitViewModel: HabitViewModel,
    contextViewModel: ContextViewModel
) {
    val habits by habitViewModel.habit.observeAsState(emptyList())
    val user by contextViewModel.user.collectAsState()
    HomeScreen(
        title = "Welcome ${user.name}",
        subtitle = "Your current habits: ${habits.size}",
        habits = habits,
        onHabitCreate = habitViewModel::createHabit,
        onHabitDelete = habitViewModel::deleteHabit,
        onHabitUpdate = habitViewModel::updateHabit,
        viewModel = contextViewModel
    )
}

@Composable
fun HomeScreen(
    title: String,
    subtitle: String,
    habits: List<Habit>,
    onHabitCreate: (Habit) -> Unit,
    onHabitDelete: (Habit) -> Unit,
    onHabitUpdate: (Habit) -> Unit,
    viewModel: ContextViewModel
) {
    val habitCounter = habits.size
    var query by remember { mutableStateOf("") }
    val filteredHabits = habits.filter {
        it.title.contains(query, ignoreCase = true) || it.description.contains(
            query,
            ignoreCase = true
        )
    }
    Scaffold(
        topBar = { SearchBar(viewModel, onQueryChange = { query = it }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = {
                val newHabit = Habit(
                    id = habits.size + 1,
                    title = "New Habit ${habitCounter + 1}",
                    description = "Description of the new habit",
                    createdAt = Timestamp(System.currentTimeMillis()),
                    updatedAt = Timestamp(System.currentTimeMillis()),
                    complete = false
                )
                onHabitCreate(newHabit)
            }) {
                Text("Add Habit")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredHabits) { habit ->
                    HabitCard(
                        habit = habit,
                        onHabitUpdate = onHabitUpdate,
                        onHabitDelete = onHabitDelete
                    )
                }
            }
        }
    }
}

