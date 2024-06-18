package com.tasks.notestodo.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.repositories.TaskRepository

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    fun addTask(note: Task) = repo.addTask(note)
    fun getTaskById(oldNote: MutableLiveData<Task>, id: Long) = repo.getTaskById(oldNote, id)
    fun updateTask(note: Task) = repo.updateTask(note)

    class NoteViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(repository) as T
        }
    }
}