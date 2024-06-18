package com.tasks.notestodo

import android.app.Application
import com.tasks.notestodo.repositories.TaskRepository
import com.tasks.notestodo.room.TasksDatabase

class MyApplication : Application() {

    private val database by lazy {
        TasksDatabase.getDatabase(this)
    }

    val repository by lazy {
        TaskRepository(database.noteDao())
    }
}