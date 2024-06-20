package com.tasks.notestodo.ui.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tasks.notestodo.R
import com.tasks.notestodo.model.Task

class TasksAdapter(
    private val inflater: LayoutInflater,
    private val onClick: (Task) -> Unit,
    private val onClickDelete: (Task) -> Unit
) :
    ListAdapter<Task, TasksAdapter.ViewHolder>(UserDiffCallback) {

    class ViewHolder(
        view: View,
        val onClick: (Task) -> Unit,
        val onClickDelete: (Task) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val deleteButton = view.findViewById<ImageView>(R.id.delete_button)
        private var note: Task? = null

        init {
            view.setOnClickListener {
                note?.let {
                    onClick(it)
                }
            }
            deleteButton.setOnClickListener {
                note?.let { note ->
                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(note.title)
                    builder.setMessage(R.string.mes_dialog)
                    builder.setPositiveButton(R.string.mes_dialog_yes) { dialog, _ ->
                        onClickDelete(note)
                        dialog.dismiss()
                    }
                    builder.setNegativeButton(R.string.mes_dialog_no) { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.create().show()
                }
            }
        }

        fun bind(note: Task) {
            this.note = note
            this.title.text = note.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.task_item, parent, false)
        return ViewHolder(view, onClick, onClickDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)

        val context = holder.itemView.context
        val backgroundColor = ContextCompat.getColor(context, task.color)
        val cardView = holder.itemView.findViewById<CardView>(R.id.cardView)
        cardView.setCardBackgroundColor(backgroundColor)
        cardView.radius = 16f

        if (task.isFavorite) {
            holder.itemView.findViewById<ImageButton>(R.id.favoriteButton)
                .setImageResource(R.drawable.baseline_favorite_24)
        } else {
            holder.itemView.findViewById<ImageButton>(R.id.favoriteButton)
                .setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    object UserDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task,
            newItem: Task
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Task,
            newItem: Task
        ) = oldItem.title == newItem.title && oldItem.text == newItem.text
    }
}