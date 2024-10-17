package com.example.daytracker.ui.screens

import HabitDialog
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.daytracker.data.model.Habit
import com.example.daytracker.ui.components.ExpandedHabitView
import com.example.daytracker.ui.components.HabitItem
import com.example.daytracker.ui.components.SearchBar
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.HabitViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Home(
    habitViewModel: HabitViewModel,
    contextViewModel: ContextViewModel
) {
    val habits by habitViewModel.habit.observeAsState(emptyList())
    HomeScreen(
        habits = habits,
        onHabitCreate = habitViewModel::createHabit,
        onHabitDelete = habitViewModel::deleteHabit,
        onHabitUpdate = habitViewModel::updateHabit,
        viewModel = contextViewModel,
        isPremiumUser = true
    )
}

@Composable
fun HomeScreen(
    habits: List<Habit>,
    onHabitCreate: (Habit) -> Unit,
    onHabitDelete: (Habit) -> Unit,
    onHabitUpdate: (Habit) -> Unit,
    viewModel: ContextViewModel,
    isPremiumUser: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    var expandedHabitId by remember { mutableStateOf<Int?>(null) }
    var query by remember { mutableStateOf("") }
    val filteredHabits = habits.filter {
        it.title.contains(query, ignoreCase = true) || it.description.contains(
            query,
            ignoreCase = true
        )
    }
    val user by viewModel.user.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .blur(if (expandedHabitId != null) 10.dp else 0.dp)
        ) {
            SearchBar(viewModel, onQueryChange = { query = it })
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome ${user.name}",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.Gray.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredHabits) { habit ->
                    HabitItem(
                        habit = habit,
                        onHabitUpdate = onHabitUpdate,
                        onHabitDelete = onHabitDelete,
                        onExpand = { expandedHabitId = habit.id },
                        isPremiumUser = isPremiumUser
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .size(56.dp)
                .zIndex(if (expandedHabitId != null) -1f else 1f),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Habit")
        }

        AnimatedVisibility(
            visible = expandedHabitId != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                expandedHabitId?.let { id ->
                    val habit = habits.find { it.id == id }
                    habit?.let {
                        ExpandedHabitView(
                            habit = it,
                            onDismiss = { expandedHabitId = null },
                            onHabitUpdate = onHabitUpdate,
                            onHabitDelete = onHabitDelete,
                            isPremiumUser = isPremiumUser
                        )
                    }
                }
            }
        }

        if (showDialog) {
            HabitDialog(
                onDismiss = { showDialog = false },
                onAddHabit = { title, description ->
                    val newHabit = Habit(
                        id = habits.size + 1,
                        title = title,
                        description = description,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                        complete = false
                    )
                    onHabitCreate(newHabit)
                    showDialog = false
                }
            )
        }
    }
}

