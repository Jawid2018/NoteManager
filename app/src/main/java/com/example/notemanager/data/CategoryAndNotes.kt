package com.example.notemanager.data

import androidx.room.Embedded
import androidx.room.Relation
import java.util.*

data class CategoryAndNotes(

    @Embedded
    val category: Category,

    @Relation(parentColumn = "id", entityColumn = "category_id")
    val notes: List<Note>

) {
    var categoryId: String? = UUID.randomUUID().toString()
}