package com.snes.snapnotes.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.snes.snapnotes.MyApplication
import com.snes.snapnotes.R
import com.snes.snapnotes.databinding.ActivityNoteBinding
import com.snes.snapnotes.model.Note
import com.snes.snapnotes.ui.viewmodels.NoteViewModel
import java.util.Date

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private val viewModel: NoteViewModel by viewModels() {
        NoteViewModel.NoteViewModelFactory((application as MyApplication).repository)
    }
    private var editMode = false
    private val oldNote = MutableLiveData<Note>()
    private var isFavorite = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNote()
        setControlButton()

    }

    private fun findNote() {
        val id = intent.getLongExtra("task_id", -1L)
        editMode = id != -1L

        if (editMode)
            viewModel.getTaskById(oldNote, id)

        oldNote.observe(this) {
            it?.let {
                binding.title.editText?.setText(it.title)
                binding.text.editText?.setText(it.text)
            } ?: throw IllegalArgumentException("task_id is incorrect")
        }
    }

    private fun setControlButton() {
        val imageResources = listOf(
            R.drawable.image_note_1,
            R.drawable.image_note_2,
            R.drawable.image_note_3
        )
        val randomImageResource = imageResources.random()
        val imageUri = Uri.parse("android.resource://$packageName/$randomImageResource")
        binding.save.setOnClickListener {
            val date = Date()
            if (editMode) {
                oldNote.value?.let {
                    it.title = binding.title.editText?.text.toString()
                    it.text = binding.text.editText?.text.toString()
                    it.updated = date
                    it.isFavorite = isFavorite
                    it.image = imageUri
                    viewModel.updateTask(it)
                }
            } else {
                val task = Note(
                    title = binding.title.editText?.text.toString(),
                    text = binding.text.editText?.text.toString(),
                    created = date,
                    updated = date,
                    isFavorite = isFavorite,
                    image = imageUri
                )
                viewModel.addTask(task)
            }
            finish()
        }
        binding.clear.setOnClickListener {
            binding.title.editText?.text?.clear()
            binding.text.editText?.text?.clear()
        }
    }
}