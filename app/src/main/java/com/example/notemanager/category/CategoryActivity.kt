package com.example.notemanager.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.notemanager.R
import com.example.notemanager.adapter.CategoryAdapter
import com.example.notemanager.data.CategoryRepository
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {

    private lateinit var viewModel: CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)

        val factory = CategoryViewModelFactory(CategoryRepository.getInstance(application))
        viewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)

        val adapter = CategoryAdapter()
        recycler_view_category.adapter = adapter

        subscribeUI(adapter)

    }

    private fun subscribeUI(adapter: CategoryAdapter) {
        supportActionBar?.title = getString(R.string.note_categories)
        viewModel.categoryList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> {
                onNavigateUp()
                true
            }

        }
    }
}
