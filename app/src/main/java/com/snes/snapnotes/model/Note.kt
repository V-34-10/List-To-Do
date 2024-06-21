package com.snes.snapnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var title: String = "",
    var text: String = "",
    var color: Int = android.R.color.white,
    var created: Date = Date(),
    var updated: Date = Date(),
    var isFavorite: Boolean = false
)