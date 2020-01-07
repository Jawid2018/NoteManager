package com.example.notemanager.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.notemanager.data.CategoryAndNotes
import com.example.notemanager.data.CategoryRepository

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    val categoryList: LiveData<PagedList<CategoryAndNotes>> =
        categoryRepository.getCategoryAndNotes()
}