package com.example.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Close
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalFocusManager
import com.example.tasktracker.ui.TaskViewModel
import com.example.tasktracker.ui.components.TaskList
import com.example.tasktracker.ui.components.TaskDialog
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import com.example.tasktracker.data.Task
import androidx.compose.foundation.interaction.MutableInteractionSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTrackerTheme {
                TaskApp(TaskViewModel(applicationContext))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var selectedTasks by remember { mutableStateOf(setOf<String>()) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (isSelectionMode) {
                TopAppBar(
                    title = { Text("Выбрано: ${selectedTasks.size}") },
                    actions = {
                        IconButton(onClick = {
                            selectedTasks.forEach { viewModel.deleteTask(it) }
                            isSelectionMode = false
                            selectedTasks = emptySet()
                        }) {
                            Icon(Icons.Rounded.Delete, "Удалить выбранное")
                        }
                        IconButton(onClick = {
                            isSelectionMode = false
                            selectedTasks = emptySet()
                        }) {
                            Icon(Icons.Rounded.Close, "Отменить")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Добавить задачу")
                }
            }
        }
    ) { padding ->
        TaskList(
            tasks = tasks,
            selectedTasks = selectedTasks,
            isSelectionMode = isSelectionMode,
            onTaskClick = { taskId ->
                if (isSelectionMode) {
                    selectedTasks = if (taskId in selectedTasks) {
                        selectedTasks - taskId
                    } else {
                        selectedTasks + taskId
                    }
                } else {
                    editingTask = tasks.find { it.id == taskId }
                    showDialog = true
                }
            },
            onTaskLongClick = { taskId ->
                if (!isSelectionMode) {
                    isSelectionMode = true
                    selectedTasks = setOf(taskId)
                }
            },
            onTaskComplete = { viewModel.toggleTaskCompletion(it) },
            modifier = Modifier.padding(padding)
        )
        
        if (showDialog) {
            TaskDialog(
                onDismiss = {
                    showDialog = false
                    editingTask = null
                },
                onTaskCreate = { task ->
                    if (editingTask != null) {
                        viewModel.updateTask(task)
                    } else {
                        viewModel.addTask(task)
                    }
                },
                task = editingTask
            )
        }
    }
}