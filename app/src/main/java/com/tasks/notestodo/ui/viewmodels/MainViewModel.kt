package com.tasks.notestodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel(private val repo: TaskRepository) : ViewModel() {

    val notes = repo.tasks

    fun delete(task: Task) = repo.delete(task)

    fun searchNotes(query: String): Flow<List<Task>> = flow {
        if (query.isBlank()) {
            emit(repo.tasks.value ?: emptyList())
        } else {
            emit(repo.searchNotes(query))
        }
    }

    class MainViewModelFactory(private val repo: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repo) as T
        }
    }
}