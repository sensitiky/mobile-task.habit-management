package com.example.daytracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.daytracker.data.model.Tasks
import java.util.Calendar

class TasksViewModel : ViewModel() {
    private val _task = MutableLiveData<List<Tasks>>(emptyList())
    val task: LiveData<List<Tasks>> = _task

    fun createTask(task: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply { add(task) }
    }

    fun deleteTask(task: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply { remove(task) }
    }

    fun updateTask(updatedTask: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply {
            val index = indexOfFirst { it.id == updatedTask.id }
            if (index != -1) set(index, updatedTask)
        }
    }
    fun getCompletedTasksPerDay(): List<Pair<String, Int>> {
        val tasks = task.value ?: return emptyList()
        val calendar = Calendar.getInstance()
        val data = mutableListOf<Pair<String, Int>>()
        for (i in 29 downTo 0) {
            val day = calendar.clone() as Calendar
            day.add(Calendar.DAY_OF_MONTH, -i)
            val dayStr = "${day.get(Calendar.DAY_OF_MONTH)}/${day.get(Calendar.MONTH) + 1}"
            val tasksCompleted = tasks.count {
                it.completedDate?.let { date ->
                    val taskCal = Calendar.getInstance().apply { time = date }
                    taskCal.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH) &&
                            taskCal.get(Calendar.MONTH) == day.get(Calendar.MONTH) &&
                            taskCal.get(Calendar.YEAR) == day.get(Calendar.YEAR)
                } ?: false
            }
            data.add(Pair(dayStr, tasksCompleted))
        }
        return data
    }
}