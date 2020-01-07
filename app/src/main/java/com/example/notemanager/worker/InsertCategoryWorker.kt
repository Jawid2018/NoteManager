package com.example.notemanager.worker

import android.app.Application
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notemanager.data.AppDatabase
import com.example.notemanager.data.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsertCategoryWorker constructor(
    context: Application,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val categoryDao = AppDatabase.getInstance(context = applicationContext).categoryDao()

        val shopping = Category(categoryName = "Shopping")
        val personal = Category(categoryName = "Personal")
        val office = Category(categoryName = "Office")
        val college = Category(categoryName = "College")

        val list = listOf(
            shopping, personal, office, college
        )

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                categoryDao.insert(list)
            }
        }
        return Result.success()
    }
}