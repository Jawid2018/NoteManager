package com.example.notemanager.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.notemanager.data.NoteAndCategory

class NoteListAdapter(private val onNoteItemClickListener: OnNoteItemClickListener) :
    PagedListAdapter<NoteAndCategory, NoteViewHolder>(NoteItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), onNoteItemClickListener = onNoteItemClickListener)
    }
}

class OnNoteItemClickListener(
    private val clickListener: (id: Long?) -> Unit,
    private val checkBoxListener: (id: Long?) -> Unit
) {
    fun onClick(id: Long?) = clickListener(id)
    fun onCheckboxChecked(id: Long?) = checkBoxListener(id)
}

class NoteItemCallBack : DiffUtil.ItemCallback<NoteAndCategory>() {
    override fun areItemsTheSame(oldItem: NoteAndCategory, newItem: NoteAndCategory): Boolean {
        return oldItem.note.id == newItem.note.id
    }

    override fun areContentsTheSame(oldItem: NoteAndCategory, newItem: NoteAndCategory): Boolean {
        return oldItem == newItem
    }

}