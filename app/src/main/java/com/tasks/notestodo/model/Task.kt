package com.tasks.notestodo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var title: String = "",
    var text: String = "",
    var created: Date = Date(),
    var updated: Date = Date()
)