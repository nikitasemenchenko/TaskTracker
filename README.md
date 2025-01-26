# TaskTracker

TaskTracker is a modern Android task management application built with Jetpack Compose and following Material Design 3 principles.

## Features

- 📝 Create, edit and delete tasks
- ✅ Mark tasks as completed
- 📅 Set due dates with time
- 🔍 Multi-select tasks for bulk deletion
- 💾 Local data storage
- 🌓 Light and dark theme support
- 📱 Modern Material Design 3

## Technologies

- **Kotlin** - primary programming language
- **Jetpack Compose** - modern toolkit for building native UI
- **Material Design 3** - Google's design system
- **ViewModel** - for managing UI data
- **Kotlin Coroutines & Flow** - for asynchronous operations
- **JSON** - for data storage

## Architecture

The application is built using MVVM (Model-View-ViewModel) principles:
- **Model**: Represented by the `Task` class and data storage logic
- **View**: UI components built with Jetpack Compose
- **ViewModel**: `TaskViewModel` for state management and business logic

## Functionality

### Task Management
- Create new tasks with title and description
- Set due dates
- Mark tasks as completed
- Edit existing tasks
- Delete tasks

### Multi-select
- Long press activates selection mode
- Ability to select multiple tasks
- Bulk delete selected tasks

### Sorting
- Automatic sorting by due date
- Tasks without due dates appear at the end of the list

## 📸 Screenshots  

![App Screenshot](https://github.com/nikitasemenchenko/TaskTracker/blob/assets/Screenshot.png)  

## Installation

1. Clone the repository:
    git clone [https://github.com/yourusername/tasktracker.git](https://github.com/nikitasemenchenko/TaskTracker.git)

2. Open project in Android Studio

3. Run the app on an emulator or real device

## Requirements

- Android 6.0 (API level 23) or higher
- Android Studio Arctic Fox or newer
