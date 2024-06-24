package com.snes.snapnotes.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snes.snapnotes.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updated DESC")
    fun getAllTasks(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getTaskById(id: Long): Note

    @Update
    fun updateTask(task: Note)

    @Delete
    fun delete(task: Note)

    @Query("SELECT * FROM notes WHERE title LIKE :query OR text LIKE :query")
    suspend fun searchNotes(query: String): List<Note>

    @Query("SELECT * FROM notes ORDER BY created ASC")
    fun getAllTasksByDateCreated(): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY updated ASC")
    fun getAllTasksByDateModified(): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY title ASC")
    fun getAllTasksByTitle(): Flow<List<Note>>

    @Query("UPDATE notes SET isFavorite = :isFavorite WHERE id = :taskId")
    suspend fun updateFavoriteStatus(taskId: Long, isFavorite: Boolean)

    @Query("SELECT * FROM notes WHERE isFavorite = 1 ORDER BY updated DESC")
    fun getFavoriteTasks(): Flow<List<Note>>
}