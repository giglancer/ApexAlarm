package com.apexalramapp.player

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.apexalramapp.preference.DataStore.mediaDataStore
import com.apexalramapp.preference.PreferenceKeys
import com.apexalramapp.utils.Notification
import com.apexalramapp.utils.SettingMedia
import com.apexalramapp.utils.cancelNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Player(context: Context) {
    private val mediaPlayer: MediaPlayer? = MediaPlayer.create(context, SettingMedia.setMedia(context))
    private val readMediaPlayFlg: Flow<Boolean?> = context.applicationContext.mediaDataStore.data.map {
            pref ->
        pref[PreferenceKeys.mediaPlayFlg] ?: false
    }
    suspend fun alarmPlay() {
        readMediaPlayFlg.collect {
            if (it == true) {
                Log.d("readMediaPlayFlg", it.toString())
                Log.d("readMediaPlay", mediaPlayer.toString())

                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(1f, 1f)
                mediaPlayer?.start()
            } else {
                alarmStop()
            }
        }
    }
    fun alarmStop() {
        Notification.notificationManager.cancelNotification()
        mediaPlayer?.release()
    }
}
