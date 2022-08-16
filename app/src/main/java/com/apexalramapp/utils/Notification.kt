package com.apexalramapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.apexalramapp.MainActivity
import com.apexalramapp.R

const val CHANNEL_ID = R.string.notification_channel_id
const val NOTIFICATION_ID = 0
const val REQUEST_CODE = 0

object Notification {
    lateinit var notificationManager: NotificationManager
}

fun NotificationManager.sendNotification(context: Context) {
    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, contentIntent, PendingIntent.FLAG_IMMUTABLE)

    var builder = NotificationCompat.Builder(context, context.getString(CHANNEL_ID))
        .setSmallIcon(android.R.drawable.alert_dark_frame)
        .setContentTitle("Apex Alarm")
        .setContentText(context.getString(R.string.notification_text))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotification() {
    cancelAll()
}
