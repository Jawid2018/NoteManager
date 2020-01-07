package com.example.notemanager.new_note

import androidx.lifecycle.*
import com.example.notemanager.data.*
import com.example.notemanager.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar.*

class NewNoteViewModel(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private var _onSaveMenuClick = MutableLiveData<Event<Unit>>()
    val onSaveMenuClick: LiveData<Event<Unit>>
        get() = _onSaveMenuClick

    private var _onAddCategoryImageClick = MutableLiveData<Event<Unit>>()
    val onAddCategoryImageClick: LiveData<Event<Unit>>
        get() = _onAddCategoryImageClick

    private var _onTimeMenuClick = MutableLiveData<Event<Unit>>()
    val onTimeMenuClick: LiveData<Event<Unit>>
        get() = _onTimeMenuClick

    private var _onDateMenuClick = MutableLiveData<Event<Unit>>()
    val onDateMenuClick: LiveData<Event<Unit>>
        get() = _onDateMenuClick

    private var _noteDeleteEvent = MutableLiveData<Event<Unit>>()
    val noteDeleteEvent: LiveData<Event<Unit>>
        get() = _noteDeleteEvent

    private var dueDate = getInstance()
    private var timeHasBeenSet = false
    private var categoryId: Long = 0

    var categoryNameList: LiveData<List<String>>

    private val categoriesIndex: ArrayList<Long> = ArrayList()

    var title = MutableLiveData<String>()

    private var noteId = MutableLiveData<Long>()

    private var isNewTask = true

    val currentNote: LiveData<NoteAndCategory> = noteId.switchMap {
        noteRepository.getNoteAndCategoryById(it)
    }


    init {
        categoryNameList = Transformations.map(categoryRepository.getCategories()) {
            val name: ArrayList<String> = ArrayList()
            it.forEach { category ->
                categoriesIndex.add(category.id)
                name.add(category.categoryName)
            }
            name
        }
    }

    fun saveNote(title: String, content: String) {
        if (!timeHasBeenSet) {
            dueDate = null
        }
        val note = Note(
            title = title,
            content = content,
            categoryId = categoryId,
            dueDate = dueDate
        )


        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (isNewTask) {
                    noteRepository.saveNote(note)
                } else {
                    val note = Note(
                        id = currentNote.value?.note?.id!!,
                        title = title,
                        content = content,
                        created = getInstance(),
                        dueDate = note.dueDate,
                        categoryId = 1
                    )
                    noteRepository.updateNote(note)
                }
            }
        }
    }

    fun saveCategoryName(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.saveCategory(Category(categoryName = name))
        }
    }

    fun setCategoryId(id: Int) {
        categoryId = categoriesIndex[id]
    }

    fun setDueDate(year: Int, month: Int, day: Int) {
        timeHasBeenSet = true
        with(dueDate) {
            set(YEAR, year)
            set(MONTH, month)
            set(DAY_OF_YEAR, day)
        }
    }

    fun setTime(hour: Int, minute: Int) {
        timeHasBeenSet = true
        with(dueDate) {
            set(HOUR_OF_DAY, hour)
            set(MINUTE, minute)
        }
    }

    fun onSaveMenuClicked() {
        _onSaveMenuClick.value = Event(Unit)
    }

    fun onTimeMenuClicked() {
        _onTimeMenuClick.value = Event(Unit)
    }

    fun onDateMenuClicked() {
        _onDateMenuClick.value = Event(Unit)
    }

    fun onAddCategoryImageClicked() {
        _onAddCategoryImageClick.value = Event(Unit)
    }

    fun setNoteId(id: Long?) {
        id?.let {
            noteId.value = id
            isNewTask = false
        }
    }

    fun deleteNote() {
        _noteDeleteEvent.value = Event(Unit)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {}
            noteId.value?.let {
                noteRepository.deleteById(it)
            }
        }
    }
}

