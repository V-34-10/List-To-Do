package com.snes.snapnotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snes.snapnotes.model.Filter
import com.snes.snapnotes.model.Note
import com.snes.snapnotes.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MainViewModel(private val repo: NoteRepository) : ViewModel() {
    private val _sortMode = MutableStateFlow(Filter.DATE_CREATED)
    val sortMode: StateFlow<Filter> = _sortMode
    val notesByDateCreated = repo.tasksByDateCreated
    val notesByTitle = repo.tasksByTitle
    val notesByText = repo.tasksByText

    val favoriteNotes = repo.getFavoriteTasks()
    val notes = repo.tasks

    fun delete(task: Note) = repo.delete(task)

    fun searchNotes(query: String): Flow<List<Note>> = flow {
        if (query.isBlank()) {
            emit(repo.tasks.value ?: emptyList())
        } else {
            emit(repo.searchNotes(query))
        }
    }

    fun setFilter(mode: Filter) {
        _sortMode.value = mode
    }

    class MainViewModelFactory(private val repo: NoteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repo) as T
        }
    }
}