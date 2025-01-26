package com.example.tasktracker.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import org.json.JSONArray
import org.json.JSONObject

class TaskViewModel(private val context: Context) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    init {
        viewModelScope.launch {
            _tasks.value = loadTasks()
        }
    }

    fun addTask(task: Task) {
        _tasks.update { currentTasks ->
            (currentTasks + task).sortedBy { it.dueDate ?: Long.MAX_VALUE }
        }
        saveTasks()
    }

    fun deleteTask(taskId: String) {
        _tasks.update { currentTasks ->
            currentTasks.filter { it.id != taskId }
        }
        saveTasks()
    }

    fun updateTask(task: Task) {
        _tasks.update { currentTasks ->
            currentTasks.map { if (it.id == task.id) task else it }
                .sortedBy { it.dueDate ?: Long.MAX_VALUE }
        }
        saveTasks()
    }

    fun toggleTaskCompletion(taskId: String) {
        _tasks.update { currentTasks ->
            currentTasks.map {
                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted)
                else it
            }
        }
        saveTasks()
    }

    private fun saveTasks() {
        val jsonArray = JSONArray()
        _tasks.value.forEach { task ->
            val jsonObject = JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("description", task.description)
                put("dueDate", task.dueDate ?: JSONObject.NULL)
                put("isCompleted", task.isCompleted)
                put("createdAt", task.createdAt)
            }
            jsonArray.put(jsonObject)
        }
        
        context.openFileOutput("tasks.json", Context.MODE_PRIVATE).use {
            it.write(jsonArray.toString().toByteArray())
        }
    }

    private fun loadTasks(): List<Task> {
        return try {
            val file = context.openFileInput("tasks.json")
            val jsonString = file.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val tasks = mutableListOf<Task>()
            
            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                tasks.add(Task(
                    id = json.getString("id"),
                    title = json.getString("title"),
                    description = json.getString("description"),
                    dueDate = if (json.isNull("dueDate")) null else json.getLong("dueDate"),
                    isCompleted = json.getBoolean("isCompleted"),
                    createdAt = json.getLong("createdAt")
                ))
            }
            tasks.sortedBy { it.dueDate ?: Long.MAX_VALUE }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
