package com.snes.snapnotes.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snes.snapnotes.model.Note
import com.snes.snapnotes.room.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val taskDao: NoteDao) {

    val tasks: LiveData<List<Note>> = taskDao.getAllTasks()
    val tasksByDateCreated: Flow<List<Note>> = taskDao.getAllTasksByDateCreated()
    val tasksByTitle: Flow<List<Note>> = taskDao.getAllTasksByTitle()
    val tasksByText: Flow<List<Note>> = taskDao.getAllTasksByText()
    fun addTask(note: Note) = Thread { taskDao.insert(note) }.start()

    fun getTaskById(oldNote: MutableLiveData<Note>, id: Long) {
        Thread {
            taskDao.getTaskById(id).let {
                oldNote.postValue(it)
            }
        }.start()
    }

    suspend fun searchNotes(query: String): List<Note> {
        return taskDao.searchNotes("%$query%")
    }

    fun updateTask(note: Note) = Thread { taskDao.updateTask(note) }.start()

    fun delete(note: Note) = Thread { taskDao.delete(note) }.start()
    fun getFavoriteTasks(): Flow<List<Note>> = taskDao.getFavoriteTasks()
}