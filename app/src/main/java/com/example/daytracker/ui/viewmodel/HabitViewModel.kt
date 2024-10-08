package com.example.daytracker.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytracker.data.model.Habit
import com.example.daytracker.data.repository.HabitRepository
import kotlinx.coroutines.launch

class HabitViewModel(context: Context) : ViewModel() {
    private val habitRepository = HabitRepository(context)
    private val _habit = MutableLiveData(habitRepository.getHabitsBlocking())
    val habit: LiveData<List<Habit>> = _habit

    fun createHabit(habit: Habit) {
        _habit.value = _habit.value?.toMutableList()?.apply { add(habit) }
        saveHabits()
    }

    fun deleteHabit(habit: Habit) {
        _habit.value = _habit.value?.toMutableList()?.apply { remove(habit) }
        saveHabits()
    }

    fun updateHabit(updateHabit: Habit) {
        _habit.value = _habit.value?.toMutableList()?.apply {
            val index = indexOfFirst { it.id == updateHabit.id }
            set(index, updateHabit)
        }
        saveHabits()
    }

    private fun saveHabits() {
        viewModelScope.launch {
            _habit.value?.let { habitRepository.saveHabits(it) }
        }
    }
}