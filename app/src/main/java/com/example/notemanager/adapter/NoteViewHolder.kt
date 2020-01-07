package com.example.notemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notemanager.R
import com.example.notemanager.data.NoteAndCategory
import kotlinx.android.synthetic.main.note_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class NoteViewHolder private constructor(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(noteAndCategory: NoteAndCategory?, onNoteItemClickListener: OnNoteItemClickListener) {
        view.tv_content.text = noteAndCategory?.note?.content
        view.tv_title.text = noteAndCategory?.note?.title
        view.tv_categoru.text = noteAndCategory?.category?.categoryName

        if (noteAndCategory?.note?.dueDate != null) {
            view.tv_due_date.text = dueDateFormat.format(noteAndCategory.note.dueDate.timeInMillis)
            view.cb_done.visibility = View.VISIBLE
        } else {
            view.cb_done.visibility = View.GONE
            view.tv_due_date.visibility = View.GONE
        }

        view.setOnClickListener {
            onNoteItemClickListener.onClick(noteAndCategory?.note?.id)
        }

        view.cb_done.setOnCheckedChangeListener { _, _ ->
            onNoteItemClickListener.onCheckboxChecked(noteAndCategory?.note?.id)
        }

    }

    companion object {
        fun from(parent: ViewGroup): NoteViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return NoteViewHolder(inflater.inflate(R.layout.note_item_layout, parent, false))
        }

        val dueDateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm:ss", Locale.US)
    }
}