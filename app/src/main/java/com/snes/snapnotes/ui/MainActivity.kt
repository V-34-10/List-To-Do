package com.snes.snapnotes.ui

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
import com.snes.snapnotes.MyApplication
import com.snes.snapnotes.databinding.ActivityMainBinding
import com.snes.snapnotes.model.Filter
import com.snes.snapnotes.ui.adapters.NotesAdapter
import com.snes.snapnotes.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: NotesAdapter
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setRecycler()
        createNote()
        setSearch()
        setFilter()
    }

    private fun setSearch() {
        binding.search.addTextChangedListener { text ->
            lifecycleScope.launch {
                viewModel.searchNotes(text.toString()).collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun createNote() {
        binding.fab.setOnClickListener { startActivity(Intent(this, NoteActivity::class.java)) }
    }

    private fun setRecycler() {
        binding.list.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(
            layoutInflater
        ) { task ->
            val i = Intent(this, NoteActivity::class.java)
            i.putExtra("task_id", task.id)
            startActivity(i)
        }
        binding.list.adapter = adapter

        viewModel.notes.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun setFilter() {
        val sortOptions = arrayOf("Date Created", "Title", "Text")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = adapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.setFilter(Filter.DATE_CREATED)
                    1 -> viewModel.setFilter(Filter.TITLE)
                    2 -> viewModel.setFilter(Filter.TEXT)
                }
                updateNotesBasedOnSortMode()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateNotesBasedOnSortMode() {
        lifecycleScope.launch {
            when (viewModel.sortMode.value) {
                Filter.DATE_CREATED -> viewModel.notesByDateCreated.collect {
                    adapter.submitList(
                        it
                    )
                }


                Filter.TITLE -> viewModel.notesByTitle.collect { adapter.submitList(it) }
                Filter.TEXT -> viewModel.notesByText.collect { adapter.submitList(it) }
            }
        }
    }
}