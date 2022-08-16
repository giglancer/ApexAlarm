package com.apexalramapp.worker

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.apexalramapp.player.Player
import com.apexalramapp.utils.Notification
import com.apexalramapp.utils.sendNotification

class MediaPlayerStartWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    private val cxt = context
    private val player = Player(cxt)
    override suspend fun doWork(): Result {
        Notification.notificationManager = cxt.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return try {
            Notification.notificationManager.sendNotification(cxt)
            player.alarmPlay()
            Result.success()
        } catch (throwable : Throwable) {
            player.alarmStop()
            Log.e("Error throwable:", "$throwable")
            Result.failure()
        }
    }
}