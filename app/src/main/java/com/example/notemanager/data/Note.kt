package com.example.notemanager.data

import androidx.room.*
import java.util.*


@Entity(
    tableName = "note_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"]
    )],
    indices = [Index("category_id")]
)

data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val created: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "due_date")
    val dueDate: Calendar? = null,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    val completed: Int = 0
) {
    fun hasCompleted(): Boolean? = completed == 1
}