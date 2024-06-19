package com.tasks.notestodo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.tasks.notestodo.MyApplication
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findEditTask()
        initControlButton()
        choiceSetColor()
        updateColorSelection()
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
                    viewModel.updateTask(it)
                }
            } else {
                val task = Task(
                    title = binding.title.editText?.text.toString(),
                    text = binding.text.editText?.text.toString(),
                    color = selectedColor,
                    created = date,
                    updated = date
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
    }

    private fun choiceSetColor() {
        binding.colorRed.setOnClickListener {
            selectedColor = android.R.color.holo_red_light
            updateColorSelection()
        }

        binding.colorGreen.setOnClickListener {
            selectedColor = android.R.color.holo_green_light
            updateColorSelection()
        }

        binding.colorBlue.setOnClickListener {
            selectedColor = android.R.color.holo_blue_light
            updateColorSelection()
        }

    }

    private fun updateColorSelection() {
        binding.colorRed.setBackgroundResource(0) // Set background to null
        binding.colorGreen.setBackgroundResource(0)
        binding.colorBlue.setBackgroundResource(0)

        // Now set alpha for the ImageButtons themselves:
        binding.colorRed.alpha = if (selectedColor == android.R.color.holo_red_light) 1.0f else 0.5f
        binding.colorGreen.alpha =
            if (selectedColor == android.R.color.holo_green_light) 1.0f else 0.5f
        binding.colorBlue.alpha =
            if (selectedColor == android.R.color.holo_blue_light) 1.0f else 0.5f
    }
}