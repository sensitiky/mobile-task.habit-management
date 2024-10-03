package com.example.daytracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.daytracker.data.model.Habit

class HabitViewModel(initialHabits: List<Habit>) : ViewModel() {
    private val _habit = MutableLiveData<List<Habit>>(initialHabits)
    val habit: LiveData<List<Habit>> = _habit

    fun createHabit(habit: Habit) {
        _habit.value = _habit.value?.toMutableList()?.apply { add(habit) }
    }

    fun deleteHabit(habit: Habit) {
        _habit.value=_habit.value?.toMutableList()?.apply { remove(habit) }
    }

    fun updateHabit(updateHabit: Habit) {
        _habit.value = _habit.value?.toMutableList()?.apply {
            val index = indexOfFirst { it.id == updateHabit.id }
            set(index, updateHabit)
        }
    }
}