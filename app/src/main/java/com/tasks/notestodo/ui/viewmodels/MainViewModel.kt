package com.tasks.notestodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tasks.notestodo.model.SortMode
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(private val repo: TaskRepository) : ViewModel() {
    private val _sortMode = MutableStateFlow(SortMode.DATE_CREATED)
    val sortMode: StateFlow<SortMode> = _sortMode

    val notesByDateCreated = repo.tasksByDateCreated
    val notesByDateModified = repo.tasksByDateModified
    val notesByTitle = repo.tasksByTitle

    val favoriteNotes = repo.getFavoriteTasks()
    val notes = repo.tasks

    fun delete(task: Task) = repo.delete(task)

    fun searchNotes(query: String): Flow<List<Task>> = flow {
        if (query.isBlank()) {
            emit(repo.tasks.value ?: emptyList())
        } else {
            emit(repo.searchNotes(query))
        }
    }

    fun setSortMode(mode: SortMode) {
        _sortMode.value = mode
    }

    class MainViewModelFactory(private val repo: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repo) as T
        }
    }
}