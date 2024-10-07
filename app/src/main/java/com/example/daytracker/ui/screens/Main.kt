package com.example.daytracker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daytracker.R
import com.example.daytracker.data.model.Habit
import com.example.daytracker.ui.components.BottomAppBarState
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.HabitViewModel

@Composable
fun MainScreen(viewModel: ContextViewModel) {
    val navController = rememberNavController()
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
                val imagePainter = painterResource(id = R.drawable.tracker)
                val habits = remember { mutableStateListOf<Habit>() }
                Home(imagePainter, habitViewModel = HabitViewModel(habits), viewModel)
            }
            composable("tasks") {
                TasksScreen()
            }
            composable("profile") {
                ProfileScreen(viewModel)
            }
        }
    }
}