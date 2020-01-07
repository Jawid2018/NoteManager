package com.example.notemanager.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notemanager.R
import com.example.notemanager.data.Note
import com.example.notemanager.home.HomeActivity
import com.example.notemanager.notification.NotificationActionReceiver
import com.example.notemanager.notification.NotificationReceiver


fun sendNotification(context: Context, note: Note) {
    val builder = NotificationCompat.Builder(
        context, NOTIFICATION_CHANNEL_ID
    ).apply {
        setSmallIcon(R.drawable.ic_note_black_24dp)
        setContentTitle(note.title)
        setContentText(note.content)
        priority = NotificationManager.IMPORTANCE_DEFAULT
        setAutoCancel(true)
        addAction(
            R.drawable.ic_edit_black_24dp,
            context.getString(R.string.edit_task),
            getEditAction(context, note.id)
        )
        addAction(
            R.drawable.ic_done_black_24dp,
            context.getString(R.string.task_done),
            getDoneAction(context, note.id)
        )
        setContentIntent(getContentIntent(context, note.id))
    }

    with(NotificationManagerCompat.from(context)) {
        notify(note.id.toInt(), builder.build())
    }
}


fun createChannel(
    context: Context,
    channelId: String,
    channelName: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            description = context.getString(R.string.channel_description)
            lightColor = Color.RED
        }

        with(NotificationManagerCompat.from(context)) {
            createNotificationChannel(channel)
        }
    }
}

fun scheduleNotification(context: Context, triggerTime: Long, noteId: Long) {
    val alarmManager = ContextCompat.getSystemService(
        context,
        AlarmManager::class.java
    ) as AlarmManager

    AlarmManagerCompat.setExactAndAllowWhileIdle(
        alarmManager,
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        getPendingIntent(context, noteId)
    )


}

private fun getPendingIntent(context: Context, noteId: Long): PendingIntent {
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra(NOTE_ID, noteId)
    }
    return PendingIntent.getBroadcast(
        context,
        noteId.toInt(),
        intent, 0
    )
}


private fun getEditAction(context: Context, id: Long): PendingIntent? {
    val intent = Intent(context, NotificationActionReceiver::class.java).apply {
        putExtra(NOTE_ID, id)
        action = NOTIFICATION_ACTION_EDIT
    }
    return PendingIntent.getBroadcast(
        context,
        id.toInt(),
        intent, 0
    )

}

private fun getDoneAction(context: Context, id: Long): PendingIntent? {
    val intent = Intent(context, NotificationActionReceiver::class.java).apply {
        putExtra(NOTE_ID, id)
        action = NOTIFICATION_ACTION_DONE
    }
    return PendingIntent.getBroadcast(
        context,
        id.toInt(),
        intent,
        0
    )

}

private fun getContentIntent(context: Context, id: Long): PendingIntent? {
    val intent = Intent(context, HomeActivity::class.java)
    return PendingIntent.getActivity(
        context, id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
}
