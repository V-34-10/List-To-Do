package com.tasks.notestodo.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.room.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val tasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val tasksByDateCreated: Flow<List<Task>> = taskDao.getAllTasksByDateCreated()
    val tasksByDateModified: Flow<List<Task>> = taskDao.getAllTasksByDateModified()
    val tasksByTitle: Flow<List<Task>> = taskDao.getAllTasksByTitle()
    fun addTask(note: Task) = Thread { taskDao.insert(note) }.start()

    fun getTaskById(oldNote: MutableLiveData<Task>, id: Long) {
        Thread {
            taskDao.getTaskById(id).let {
                oldNote.postValue(it)
            }
        }.start()
    }

    suspend fun searchNotes(query: String): List<Task> {
        return taskDao.searchNotes("%$query%")
    }

    fun updateTask(note: Task) = Thread { taskDao.updateTask(note) }.start()

    fun delete(note: Task) = Thread { taskDao.delete(note) }.start()
}