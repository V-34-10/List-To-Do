package com.snes.snapnotes.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snes.snapnotes.model.Note
import com.snes.snapnotes.repositories.NoteRepository

class NoteViewModel(private val repo: NoteRepository) : ViewModel() {

    fun addTask(note: Note) = repo.addTask(note)
    fun getTaskById(oldNote: MutableLiveData<Note>, id: Long) = repo.getTaskById(oldNote, id)
    fun updateTask(note: Note) = repo.updateTask(note)

    class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteViewModel(repository) as T
        }
    }
}