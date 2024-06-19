package com.tasks.notestodo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.notestodo.MyApplication
import com.tasks.notestodo.databinding.ActivityMainBinding
import com.tasks.notestodo.ui.adapters.TasksAdapter
import com.tasks.notestodo.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
        initFab()
    }

    private fun initRecycler() {
        binding.list.layoutManager = LinearLayoutManager(this)
        val adapter = TasksAdapter(layoutInflater,
            { task ->
                val i = Intent(this, TaskActivity::class.java)
                i.putExtra("task_id", task.id)
                startActivity(i)
            },
            { task ->
                viewModel.delete(task)
            })
        binding.list.adapter = adapter

        viewModel.notes.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun initFab() {
        binding.fab.setOnClickListener { startActivity(Intent(this, TaskActivity::class.java)) }
    }
}