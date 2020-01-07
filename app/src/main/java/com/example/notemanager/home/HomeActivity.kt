package com.example.notemanager.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.notemanager.R
import com.example.notemanager.adapter.NoteListAdapter
import com.example.notemanager.adapter.OnNoteItemClickListener
import com.example.notemanager.category.CategoryActivity
import com.example.notemanager.data.NoteRepository
import com.example.notemanager.login.LoginActivity
import com.example.notemanager.new_note.NewNoteActivity
import com.example.notemanager.settings.SettingsActivity
import com.example.notemanager.utils.EventObserver
import com.example.notemanager.utils.NOTE_ID
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setSupportActionBar(toolbar)

        val factory = HomeViewModelFactory(NoteRepository.getInstance(application))
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        fab.setOnClickListener {
            viewModel.onFabClicked()
        }

        val adapter = NoteListAdapter(OnNoteItemClickListener(clickListener = {
            it?.let { viewModel.onNoteItemClick(it) }

        }, checkBoxListener = {
            it?.let {
                viewModel.updateNote(it)
            }
        }))

        note_recycler_view.adapter = adapter

        subscribeUI(adapter)

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun subscribeUI(adapter: NoteListAdapter) {
        viewModel.onFabClick.observe(this, EventObserver {
            navigateToNewNoteActivity()
        })

        viewModel.noteList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.onNoteItemClick.observe(this, EventObserver {
            val intent = Intent(this, NewNoteActivity::class.java).apply {
                putExtra(NOTE_ID, it)
            }
            startActivity(intent)
        })

        viewModel.onCateMenuClick.observe(this, EventObserver {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        })

        viewModel.onSettingsMenuClick.observe(this, EventObserver {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        })

        viewModel.onLogoutMenuClick.observe(this, EventObserver {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean(getString(R.string.key_pref_user_login), false)
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        viewModel.isDataAvailable.observe(this, Observer {
            if (it) {
                tv_empty.visibility = View.GONE
            } else tv_empty.visibility = View.VISIBLE

        })

    }


    private fun navigateToNewNoteActivity() {
        val intent = Intent(this, NewNoteActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort -> sortContents()
            R.id.menu_logout -> viewModel.onLogoutMenuClick()
            R.id.menu_all -> viewModel.setFilterType(NoteFilterType.ALL)
            R.id.menu_notes -> viewModel.setFilterType(NoteFilterType.NOTES)
            R.id.menu_tasks -> viewModel.setFilterType(NoteFilterType.TASKS)
            R.id.menu_completed -> viewModel.setFilterType(NoteFilterType.COMPLETED)
            else -> drawer.openDrawer(GravityCompat.START)

        }
        return true
    }

    private fun sortContents() {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_category -> viewModel.onCategoryMenuClicked()
            R.id.menu_statistic -> navigateToStatisticsActivity()
            R.id.menu_setting -> viewModel.onSettingsMenuClicked()
            R.id.menu_logout -> viewModel.onLogoutMenuClick()
        }
        return true
    }

    private fun navigateToStatisticsActivity() {

    }
}

enum class NoteFilterType { ALL, NOTES, TASKS, COMPLETED }