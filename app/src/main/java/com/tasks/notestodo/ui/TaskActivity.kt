package com.tasks.notestodo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.tasks.notestodo.MyApplication
import com.tasks.notestodo.R
import com.tasks.notestodo.databinding.ActivityTaskBinding
import com.tasks.notestodo.model.Task
import com.tasks.notestodo.ui.viewmodels.TaskViewModel
import java.util.Date

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private val viewModel: TaskViewModel by viewModels() {
        TaskViewModel.NoteViewModelFactory((application as MyApplication).repository)
    }
    private var editMode = false
    private val oldNote = MutableLiveData<Task>()
    private var selectedColor: Int = android.R.color.white
    private var isFavorite = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findEditTask()
        initControlButton()
        choiceSetColor()
        updateColorSelection()

        oldNote.observe(this) { task ->
            task?.let {
                isFavorite = it.isFavorite
                updateFavoriteButtonState()
            }
        }
    }

    private fun findEditTask() {
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

    private fun initControlButton() {
        binding.save.setOnClickListener {
            val date = Date()
            if (editMode) {
                oldNote.value?.let {
                    it.title = binding.title.editText?.text.toString()
                    it.text = binding.text.editText?.text.toString()
                    it.color = selectedColor
                    it.updated = date
                    it.isFavorite = isFavorite
                    viewModel.updateTask(it)
                }
            } else {
                val task = Task(
                    title = binding.title.editText?.text.toString(),
                    text = binding.text.editText?.text.toString(),
                    color = selectedColor,
                    created = date,
                    updated = date,
                    isFavorite = isFavorite
                )
                viewModel.addTask(task)
            }
            finish()
        }
        binding.clear.setOnClickListener {
            binding.title.editText?.text?.clear()
            binding.text.editText?.text?.clear()
            selectedColor = android.R.color.white
            updateColorSelection()
        }
        binding.favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteButtonState()
        }
    }

    private fun updateFavoriteButtonState() {
        if (isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun choiceSetColor() {
        binding.colorRed.setOnClickListener {
            selectedColor = R.color.red
            updateColorSelection()
        }
        binding.colorGreen.setOnClickListener {
            selectedColor = R.color.green
            updateColorSelection()
        }
        binding.colorBlue.setOnClickListener {
            selectedColor = R.color.blue
            updateColorSelection()
        }
        binding.colorOrange.setOnClickListener {
            selectedColor = R.color.orange
            updateColorSelection()
        }
        binding.colorPurple.setOnClickListener {
            selectedColor = R.color.purple
            updateColorSelection()
        }
    }

    private fun updateColorSelection() {
        binding.colorRed.setBackgroundResource(0)
        binding.colorGreen.setBackgroundResource(0)
        binding.colorBlue.setBackgroundResource(0)
        binding.colorOrange.setBackgroundResource(0)
        binding.colorPurple.setBackgroundResource(0)

        binding.colorRed.alpha = if (selectedColor == R.color.red) 1.0f else 0.5f
        binding.colorGreen.alpha =
            if (selectedColor == R.color.green) 1.0f else 0.5f
        binding.colorBlue.alpha =
            if (selectedColor == R.color.blue) 1.0f else 0.5f
        binding.colorOrange.alpha =
            if (selectedColor == R.color.orange) 1.0f else 0.5f
        binding.colorPurple.alpha =
            if (selectedColor == R.color.purple) 1.0f else 0.5f
    }
}