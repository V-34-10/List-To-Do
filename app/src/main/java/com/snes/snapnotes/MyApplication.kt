package com.snes.snapnotes

import android.app.Application
import com.snes.snapnotes.repositories.NoteRepository
import com.snes.snapnotes.room.NoteDatabase

class MyApplication : Application() {

    private val database by lazy {
        NoteDatabase.getDatabase(this)
    }

    val repository by lazy {
        NoteRepository(database.noteDao())
    }
}