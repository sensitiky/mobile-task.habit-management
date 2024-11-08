package com.example.daytracker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
fun MainScreen(
    contextViewModel: ContextViewModel,
    tasksViewModel: TasksViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomAppBarState(navController) },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("home") {
                val habitViewModel = HabitViewModel(LocalContext.current)
                Home(habitViewModel = habitViewModel, contextViewModel)
            }
            composable("tasks") {
                TasksScreen(taskViewModel = tasksViewModel, contextViewModel)
            }
            composable("statistics") {
                Statistics()
            }
            composable("profile") {
                ProfileScreen(contextViewModel, tasksViewModel, onNavigate = { route ->
                    navController.navigate(route)
                })
            }
        }
    }
}
