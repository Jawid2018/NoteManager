package com.example.notemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notemanager.R
import com.example.notemanager.data.CategoryAndNotes
import com.example.notemanager.data.NoteAndCategory
import kotlinx.android.synthetic.main.category_item_layout.view.*

class CategoryAdapter :
    PagedListAdapter<CategoryAndNotes, CategoryViewHolder>(CategoryItemCallback()) {

    companion object {
        class CategoryItemCallback : DiffUtil.ItemCallback<CategoryAndNotes>() {
            override fun areItemsTheSame(
                oldItem: CategoryAndNotes,
                newItem: CategoryAndNotes
            ): Boolean {
                return oldItem.category.id == newItem.category.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryAndNotes,
                newItem: CategoryAndNotes
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryViewHolder private constructor(private val view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(noteAndCategory: CategoryAndNotes?) {
        view.tv_category_name.text = noteAndCategory?.category?.categoryName.toString()
        view.tv_task_count.text = noteAndCategory?.notes?.size.toString()
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return CategoryViewHolder(
                inflater.inflate(
                    R.layout.category_item_layout,
                    parent,
                    false
                )
            )
        }
    }

}