package com.tasks.notestodo.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.room.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    val tasks: LiveData<List<Task>> = taskDao.getAllTasks()

    fun addTask(note: Task) = Thread { taskDao.insert(note) }.start()

    fun getTaskById(oldNote: MutableLiveData<Task>, id: Long) {
        Thread {
            taskDao.getTaskById(id).let {
                oldNote.postValue(it)
            }
        }.start()
    }

    fun updateTask(note: Task) = Thread { taskDao.updateTask(note) }.start()

    fun delete(note: Task) = Thread { taskDao.delete(note) }.start()
}