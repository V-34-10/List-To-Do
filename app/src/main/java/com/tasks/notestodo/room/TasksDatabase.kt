package com.tasks.notestodo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.room.converter.DateConverter

@Database(entities = [Task::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun noteDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java, "tasks_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}