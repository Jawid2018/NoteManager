package com.example.notemanager.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table WHERE completed != 1")
    fun getAllActiveNotes(): DataSource.Factory<Int, NoteAndCategory>

    @RawQuery(observedEntities = [Note::class, Category::class])
    fun getNotesSorted(query: SupportSQLiteQuery): DataSource.Factory<Int, NoteAndCategory>

    @Query("SELECT * FROM note_table WHERE id =:id")
    fun getNoteAndCategoryById(id: Long): LiveData<NoteAndCategory>

    @Query("SELECT * FROM note_table WHERE id =:id")
    fun getNoteById(id: Long): Note

    @Query("SELECT * FROM note_table ORDER BY id DESC LIMIT 1")
    fun getLatestNote(): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(vararg note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM NOTE_TABLE WHERE id =:id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM NOTE_TABLE")
    suspend fun deleteAllNotes()

    @Query("UPDATE note_table SET completed = 1 WHERE id =:id ")
    suspend fun noteDone(id: Long)

    @Update
    suspend fun update(note: Note)

}