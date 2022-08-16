package com.apexalramapp.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.apexalramapp.preference.DataStore.dataStore
import com.apexalramapp.preference.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val readSettingAlarmHour: Flow<Int?> = context.dataStore.data.map {
                pref ->
            pref[PreferenceKeys.settingAlarmHour] ?: 0
        }
        val readSettingAlarmMinute: Flow<Int?> = context.dataStore.data.map {
                pref ->
            pref[PreferenceKeys.settingAlarmMinutes] ?: 0
        }

        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { alarmIntent ->
            alarmIntent.flags = Intent.FLAG_FROM_BACKGROUND
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        }
        val scope = CoroutineScope(Job())
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            var setHour: Int? = 0
            var setMinute: Int? = 0
            scope.launch {
                readSettingAlarmHour.collect { hour ->
                    readSettingAlarmMinute.collect { minute ->
                        setHour = hour
                        setMinute = minute
                    }
                }
            }
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, setHour!!)
                set(Calendar.MINUTE, setMinute!!)
            }
            Log.d("setHour", setHour.toString())
            Log.d("setMinute", setMinute.toString())
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }
    }
}
