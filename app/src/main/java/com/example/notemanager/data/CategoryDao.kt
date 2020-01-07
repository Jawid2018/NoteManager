package com.example.notemanager.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_table")
    fun getCategoryAndNotes(): DataSource.Factory<Int, CategoryAndNotes>

    @Query("SELECT * FROM CATEGORY_TABLE")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * from category_table where id =:id")
    fun getCategoryById(id: Long): LiveData<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(category: Category)

    @Insert
    suspend fun insert(category: List<Category>)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM category_table")
    suspend fun deleteAll()

}