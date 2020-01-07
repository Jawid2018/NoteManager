package com.example.notemanager.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.notemanager.data.NoteRepository
import com.example.notemanager.new_note.NewNoteActivity
import com.example.notemanager.utils.NOTE_ID
import com.example.notemanager.utils.NOTIFICATION_ACTION_DONE
import com.example.notemanager.utils.NOTIFICATION_ACTION_EDIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.extras?.getLong(NOTE_ID)

        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }

        when (intent.action) {
            NOTIFICATION_ACTION_DONE -> setTaskCompleted(context, noteId)
            NOTIFICATION_ACTION_EDIT -> openNewNoteActivity(context, noteId)
        }


    }

    private fun openNewNoteActivity(context: Context, noteId: Long?) {
        val intent = Intent(context, NewNoteActivity::class.java).apply {
            putExtra(NOTE_ID, noteId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    private fun setTaskCompleted(context: Context, noteId: Long?) {
        noteId?.let {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    NoteRepository.getInstance(context).noteCompleted(it)
                }
            }
        }
    }
}