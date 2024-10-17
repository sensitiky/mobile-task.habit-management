package com.example.daytracker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daytracker.ui.components.BottomAppBarState
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.HabitViewModel
import com.example.daytracker.ui.viewmodel.TasksViewModel

@Composable
fun MainScreen(viewModel: ContextViewModel, task: TasksViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val habitViewModel = remember { HabitViewModel(context) }
    val taskViewModel = remember { TasksViewModel(context) }
    Scaffold(
        bottomBar = { BottomAppBarState(navController) },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                Home(habitViewModel = habitViewModel, viewModel)
            }
            composable("tasks") {
                TasksScreen(taskViewModel = taskViewModel, viewModel)
            }
            composable("profile") {
                ProfileScreen(viewModel, task, onNavigate = { route ->
                    navController.navigate(route)
                })
            }
        }
    }
}