package com.example.daytracker.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.sql.Date

class TasksViewModel(context: Context) : ViewModel() {
    private val taskRepository = TaskRepository(context)
    private val _task = MutableLiveData(taskRepository.getTasksBlocking())
    val task: LiveData<List<Tasks>> = _task

    fun createTask(task: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply { add(task) }
    }

    fun deleteTask(task: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply { remove(task) }
    }

    fun updateTask(updateTask: Tasks) {
        _task.value = _task.value?.toMutableList()?.apply {
            val index = indexOfFirst { it.id == updateTask.id }
            set(index, updateTask)
        }
        saveTask()
    }

    fun completedTask(task: Tasks) {
        val updatedTask = task.copy(completed = true, completedDate = Date(System.currentTimeMillis()))
        updateTask(updatedTask)
    }
    fun unCompletedTask(task: Tasks) {
        val updatedTask = task.copy(completed = false, completedDate = null)
        updateTask(updatedTask)
    }

    private fun saveTask() {
        viewModelScope.launch {
            _task.value?.let {
                taskRepository.saveTasks(it)
            }
        }
    }
}