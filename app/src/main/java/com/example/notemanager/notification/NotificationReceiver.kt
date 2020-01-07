package com.example.notemanager.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.notemanager.utils.NOTE_ID

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.extras?.getLong(NOTE_ID, 0L)
        noteId?.let {
            val inputData = Data.Builder().apply {
                putLong(NOTE_ID, it)
            }.build()

            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(NotificationWorker::class.java).apply {
                    setInputData(inputData)
                }.build()

            with(WorkManager.getInstance(context)) {
                enqueue(oneTimeWorkRequest)
            }
        }
    }
}