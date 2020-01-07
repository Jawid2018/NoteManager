package com.example.notemanager.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.notemanager.utils.scheduleNotification

class NoteRepository private constructor(
    private val noteDao: NoteDao
) {
    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null
        private lateinit var appContext: Context

        fun getInstance(context: Context): NoteRepository {
            appContext = context
            return INSTANCE ?: NoteRepository(AppDatabase.getInstance(context).noteDao()).also {
                INSTANCE = it
            }
        }

        private const val PAGE_SIZE = 10
    }

    fun getAllNotes(): LiveData<PagedList<NoteAndCategory>> {
        val source = noteDao.getAllActiveNotes()
        return LivePagedListBuilder(
            source, PAGE_SIZE
        ).build()
    }

    fun getNotesSorted(query: SupportSQLiteQuery): LiveData<PagedList<NoteAndCategory>> {
        val sources = noteDao.getNotesSorted(query)
        return LivePagedListBuilder(
            sources, PAGE_SIZE
        ).build()
    }

    suspend fun saveNote(note: Note) {
        noteDao.insertNotes(note)
        val note = noteDao.getLatestNote()
        note.dueDate?.let {
            scheduleNotification(
                context = appContext,
                triggerTime = it.timeInMillis,
                noteId = note.id
            )
        }

    }

    suspend fun noteCompleted(id: Long) {
        noteDao.noteDone(id)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun deleteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

    fun getNoteAndCategoryById(noteId: Long): LiveData<NoteAndCategory> {
        return noteDao.getNoteAndCategoryById(noteId)
    }

    fun getNoteById(noteId: Long): Note {
        return noteDao.getNoteById(noteId)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }
}