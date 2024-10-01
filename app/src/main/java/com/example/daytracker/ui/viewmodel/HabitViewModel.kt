package com.example.daytracker.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.example.daytracker.data.model.Habit

class HabitViewModel(initialHabits: List<Habit>) {
    public var mutableHabits = mutableStateListOf<Habit>().apply {
        addAll(initialHabits)
    }

    fun updateHabit(updateHabit: Habit) {
        val index = mutableHabits.indexOfFirst { it.id == updateHabit.id }
        if (index >= 0) {
            mutableHabits[index] = updateHabit
        }
    }
}