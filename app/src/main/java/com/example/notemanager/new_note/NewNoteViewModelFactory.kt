package com.example.notemanager.new_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notemanager.data.CategoryRepository
import com.example.notemanager.data.NoteRepository

class NewNoteViewModelFactory(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewNoteViewModel(noteRepository, categoryRepository) as T
    }
}