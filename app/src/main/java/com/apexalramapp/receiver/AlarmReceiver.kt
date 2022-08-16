package com.apexalramapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.apexalramapp.preference.DataStore.mediaDataStore
import com.apexalramapp.preference.PreferenceKeys
import com.apexalramapp.worker.MediaPlayerStartWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("onReceive", "onReceive")

        val scope = CoroutineScope(Job())
        scope.launch {
            context.mediaDataStore.edit {
                it[PreferenceKeys.mediaPlayFlg] = true
            }
        }
        val workManager = context.let { WorkManager.getInstance(it) }
        val request = OneTimeWorkRequestBuilder<MediaPlayerStartWorker>().apply {
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }.build()
        workManager.enqueue(request)
    }
}
