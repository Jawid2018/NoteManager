package com.example.notemanager.home

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.notemanager.data.NoteAndCategory
import com.example.notemanager.data.NoteRepository
import com.example.notemanager.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private var _onFabClick = MutableLiveData<Event<Unit>>()
    val onFabClick: LiveData<Event<Unit>>
        get() = _onFabClick

    private var _onNoteItemClick = MutableLiveData<Event<Long>>()
    val onNoteItemClick: LiveData<Event<Long>>
        get() = _onNoteItemClick

    private var _onCategoryMenuClick = MutableLiveData<Event<Unit>>()
    val onCateMenuClick: LiveData<Event<Unit>>
        get() = _onCategoryMenuClick

    private var _onSettingsMenuClick = MutableLiveData<Event<Unit>>()
    val onSettingsMenuClick: LiveData<Event<Unit>>
        get() = _onSettingsMenuClick

    private var _onLogoutMenuClick = MutableLiveData<Event<Unit>>()
    val onLogoutMenuClick: LiveData<Event<Unit>>
        get() = _onLogoutMenuClick

    private var filterType = MutableLiveData<NoteFilterType>()

    var noteList: LiveData<PagedList<NoteAndCategory>>

    var isDataAvailable: LiveData<Boolean>

    fun onFabClicked() {
        _onFabClick.value = Event(Unit)
    }

    fun onNoteItemClick(id: Long) {
        _onNoteItemClick.value = Event(id)
    }

    fun onCategoryMenuClicked() {
        _onCategoryMenuClick.value = Event(Unit)
    }

    init {
        setFilterType(NoteFilterType.ALL)
        noteList = switchMap(filterType) {
            getFilteredNotes(it)
        }

        isDataAvailable = noteList.map {
            noteList.value?.size != 0
        }
    }

    fun setFilterType(filter: NoteFilterType) {
        filterType.value = filter
    }

    fun updateNote(it: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                noteRepository.noteCompleted(id = it)
            }
        }
    }

    fun onSettingsMenuClicked() {
        _onSettingsMenuClick.value = Event(Unit)
    }

    fun onLogoutMenuClick() {
        _onLogoutMenuClick.value = Event(Unit)
    }

    private fun getFilteredNotes(noteFilterType: NoteFilterType): LiveData<PagedList<NoteAndCategory>> {
        val query =
            when (noteFilterType) {
                NoteFilterType.ALL -> SimpleSQLiteQuery("SELECT * FROM note_table")
                NoteFilterType.NOTES -> SimpleSQLiteQuery("SELECT * FROM note_table WHERE due_date == 'null'")
                NoteFilterType.TASKS -> SimpleSQLiteQuery("SELECT * FROM note_table WHERE due_date != 'null'")
                NoteFilterType.COMPLETED -> SimpleSQLiteQuery("SELECT * FROM note_table where completed == 1")
            }

        return noteRepository.getNotesSorted(query)
    }
}