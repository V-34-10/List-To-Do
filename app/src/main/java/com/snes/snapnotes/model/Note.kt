package com.snes.snapnotes.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var title: String = "",
    var text: String = "",
    var created: Date = Date(),
    var updated: Date = Date(),
    var isFavorite: Boolean = false,
    var image: Uri? = null
)