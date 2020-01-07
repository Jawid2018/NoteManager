package com.example.notemanager.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList


class CategoryRepository private constructor(private val categoryDao: CategoryDao) {

    companion object {
        @Volatile
        private var INSTANCE: CategoryRepository? = null

        fun getInstance(context: Context): CategoryRepository {
            return INSTANCE
                ?: CategoryRepository(AppDatabase.getInstance(context).categoryDao()).also {
                    INSTANCE = it
                }
        }

        private const val PAGE_SIZE = 20
    }

    fun getCategoryAndNotes(): LiveData<PagedList<CategoryAndNotes>> {
        val source = categoryDao.getCategoryAndNotes()
        return LivePagedListBuilder(
            source, PAGE_SIZE
        ).build()
    }

    fun getCategories(): LiveData<List<Category>> =
        categoryDao.getCategories()


    suspend fun saveCategory(category: Category) {
        categoryDao.insertAll(category)
    }

    suspend fun deleteAll() {
        categoryDao.deleteAll()
    }
}