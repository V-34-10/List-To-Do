package com.tasks.notestodo.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tasks.notestodo.model.Task
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY updated DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Task

    @Update
    fun updateTask(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE title LIKE :query OR text LIKE :query")
    suspend fun searchNotes(query: String): List<Task>

    @Query("SELECT * FROM tasks ORDER BY created ASC")
    fun getAllTasksByDateCreated(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY updated ASC")
    fun getAllTasksByDateModified(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    fun getAllTasksByTitle(): Flow<List<Task>>
}