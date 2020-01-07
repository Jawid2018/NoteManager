package com.example.notemanager.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notemanager.R
import com.example.notemanager.data.Note
import com.example.notemanager.data.NoteRepository
import com.example.notemanager.utils.NOTE_ID
import com.example.notemanager.utils.NOTIFICATION_CHANNEL_ID
import com.example.notemanager.utils.createChannel
import com.example.notemanager.utils.sendNotification

class NotificationWorker(
    val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val noteId = inputData.getLong(NOTE_ID, 0L)
        val note: Note = NoteRepository.getInstance(context = context).getNoteById(noteId)
        createChannel(
            context,
            NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.channel_name)
        )
        sendNotification(context, note)

        return Result.success()
    }
}