package com.example.notemanager.new_note

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.notemanager.R
import com.example.notemanager.data.CategoryRepository
import com.example.notemanager.data.NoteRepository
import com.example.notemanager.utils.EventObserver
import com.example.notemanager.utils.NOTE_ID
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.android.synthetic.main.edit_text_alert_dialog_layout.view.*
import kotlinx.android.synthetic.main.home_activity.toolbar
import java.util.Calendar.*

class NewNoteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: NewNoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        setSupportActionBar(toolbar)

        val factory = NewNoteViewModelFactory(
            NoteRepository.getInstance(application),
            CategoryRepository.getInstance(application)
        )
        viewModel = ViewModelProviders.of(this, factory).get(NewNoteViewModel::class.java)

        viewModel.setNoteId(intent?.extras?.getLong(NOTE_ID))

        iv_add_category.setOnClickListener {
            viewModel.onAddCategoryImageClicked()
        }

        val adapter = ArrayAdapter<String>(
            this, R.layout.support_simple_spinner_dropdown_item
        )
        sp_category_list.adapter = adapter
        sp_category_list.onItemSelectedListener = this

        subscribeUI(adapter)
    }

    private fun subscribeUI(adapter: ArrayAdapter<String>) {
        supportActionBar?.title = getString(R.string.add_new_note)
        viewModel.onSaveMenuClick.observe(this, EventObserver {
            saveNote()
        })

        viewModel.onTimeMenuClick.observe(this, EventObserver {
            displayTimeDialog()
        })

        viewModel.onDateMenuClick.observe(this, EventObserver {
            displayDateDialog()
        })

        viewModel.onAddCategoryImageClick.observe(this, EventObserver {
            displayInputDialog()
        })

        viewModel.categoryNameList.observe(this, Observer {
            it?.let {
                adapter.clear()
                adapter.addAll(it)
            }
        })

        viewModel.currentNote.observe(this, Observer {
            it?.let {
                et_title.setText(it.note.title)
                et_content.setText((it.note.content))
            }
        })

        viewModel.noteDeleteEvent.observe(this, EventObserver {
            finish()
        })
    }

    private fun displayInputDialog() {
        val view = LayoutInflater.from(this@NewNoteActivity)
            .inflate(R.layout.edit_text_alert_dialog_layout, null)

        AlertDialog.Builder(this).apply {
            setPositiveButton(R.string.ok) { _, _ ->
                if (!view.et_category_name.text.isNullOrEmpty()) {
                    viewModel.saveCategoryName(view.et_category_name.text.toString())
                }
            }

            setNegativeButton(R.string.cancel) { dialog: DialogInterface, _ ->
                dialog.cancel()
            }
            setTitle(R.string.enter_category_name)
            setView(view)
            show()
        }
    }

    private fun displayDateDialog() {
        val onDateSelectListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                viewModel.setDueDate(year, month, day)
            }

        val calendar = getInstance()
        DatePickerDialog(
            this,
            onDateSelectListener,
            calendar.get(YEAR),
            calendar.get(MONTH),
            calendar.get(DAY_OF_YEAR)
        ).apply {
            datePicker.minDate = calendar.timeInMillis
            show()
        }

    }

    private fun displayTimeDialog() {
        val onTimeSelectListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            viewModel.setTime(hour, minute)
        }
        val calendar = getInstance()
        TimePickerDialog(
            this,
            onTimeSelectListener,
            calendar.get(HOUR_OF_DAY),
            calendar.get(MINUTE),
            false
        ).run { show() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> viewModel.onSaveMenuClicked()
            R.id.menu_select_date -> viewModel.onDateMenuClicked()
            R.id.menu_select_time -> viewModel.onTimeMenuClicked()
            R.id.menu_delete -> viewModel.deleteNote()
            else -> onSupportNavigateUp()
        }
        return true
    }

    private fun saveNote() {
        if (!et_title.text.isNullOrEmpty() && !et_content.text.isNullOrEmpty()) {
            viewModel.saveNote(
                title = et_title.text.toString(),
                content = et_content.text.toString()
            )
            finish()
        } else {
            hideSoftKeyboard()
            Snackbar.make(
                findViewById(R.id.coordinator_layout),
                getString(R.string.invalid_empty),
                Snackbar.LENGTH_SHORT
            ).run { show() }
        }
    }

    private fun hideSoftKeyboard() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        viewModel.setCategoryId(p2)
    }
}
