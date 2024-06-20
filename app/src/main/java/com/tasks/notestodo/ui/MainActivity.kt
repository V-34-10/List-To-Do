package com.tasks.notestodo.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.notestodo.MyApplication
import com.tasks.notestodo.databinding.ActivityMainBinding
import com.tasks.notestodo.model.SortMode
import com.tasks.notestodo.ui.adapters.TasksAdapter
import com.tasks.notestodo.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: TasksAdapter
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
        initFab()
        initSearch()
        initSortOptions()
    }

    private fun initSearch() {
        binding.search.addTextChangedListener { text ->
            lifecycleScope.launch {
                viewModel.searchNotes(text.toString()).collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun initRecycler() {
        binding.list.layoutManager = LinearLayoutManager(this)
        adapter = TasksAdapter(layoutInflater,
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

    private fun initSortOptions() {
        val sortOptions = arrayOf("Date Created", "Date Modified", "Title", "Favorite")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = adapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.setSortMode(SortMode.DATE_CREATED)
                    1 -> viewModel.setSortMode(SortMode.DATE_MODIFIED)
                    2 -> viewModel.setSortMode(SortMode.TITLE)
                    3 -> viewModel.setSortMode(SortMode.FAVORITE)
                }
                updateNotesBasedOnSortMode()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateNotesBasedOnSortMode() {
        lifecycleScope.launch {
            when (viewModel.sortMode.value) {
                SortMode.DATE_CREATED -> viewModel.notesByDateCreated.collect {
                    adapter.submitList(
                        it
                    )
                }

                SortMode.DATE_MODIFIED -> viewModel.notesByDateModified.collect {
                    adapter.submitList(
                        it
                    )
                }

                SortMode.TITLE -> viewModel.notesByTitle.collect { adapter.submitList(it) }
                SortMode.FAVORITE -> viewModel.favoriteNotes.collect { adapter.submitList(it) }
            }
        }
    }
}