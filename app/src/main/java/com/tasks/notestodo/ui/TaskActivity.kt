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
    private val binding = ActivityTaskBinding.inflate(layoutInflater)

    private val viewModel: TaskViewModel by viewModels() {
        TaskViewModel.NoteViewModelFactory((application as MyApplication).repository)
    }
    private var editMode = false
    private val oldNote = MutableLiveData<Task>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        findEditTask()
        initControlButton()
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
                    it.updated = date
                    viewModel.updateTask(it)
                }
            } else {
                val task = Task(
                    title = binding.title.editText?.text.toString(),
                    text = binding.text.editText?.text.toString(),
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
        }
    }
}