package com.example.notemanager.data

import androidx.room.Embedded
import androidx.room.Relation

data class NoteAndCategory(
    @Embedded
    val note: Note,

    @Relation(parentColumn = "category_id", entityColumn = "id")
    val category: Category
)