package com.apexalramapp.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.* // ktlint-disable no-wildcard-imports
import com.apexalramapp.R
import com.apexalramapp.preference.DataStore.dataStore
import com.apexalramapp.preference.PreferenceKeys
import com.apexalramapp.receiver.AlarmReceiver
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class AlarmSettingFragmentViewModel(app: Application) : AndroidViewModel(app) {
    val mutableAlarmTimeText = MutableStateFlow<String?>(null)
    val alarmTimeText: StateFlow<String?> = mutableAlarmTimeText

    val diffAlarmTimeText: MutableStateFlow<String> = MutableStateFlow("")
    private val diffAlarmValues: MutableStateFlow<Map<String, Long>> = MutableStateFlow(emptyMap())

    private val readSettingAlarmFlow: Flow<String?> = app.applicationContext.dataStore.data.map {
            pref ->
        pref[PreferenceKeys.settingAlarm] ?: ""
    }
    private val readSettingAlarmHour: Flow<Int?> = app.applicationContext.dataStore.data.map {
            pref ->
        pref[PreferenceKeys.settingAlarmHour] ?: 0
    }
    private val readSettingAlarmMinute: Flow<Int?> = app.applicationContext.dataStore.data.map {
            pref ->
        pref[PreferenceKeys.settingAlarmMinutes] ?: 0
    }
    private val savedSettingAlarmTime: MutableStateFlow<String?> = MutableStateFlow("")
    val switchCompatChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val readSwitchCompatChecked: Flow<Boolean> = app.applicationContext.dataStore.data.map {
            pref ->
        pref[PreferenceKeys.switchChecked] ?: true
    }
    private val settingAlarmTextFormattedFlow: Flow<String?> = app.applicationContext.dataStore.data.map {
            pref ->
        pref[PreferenceKeys.settingAlarmTextFormatted] ?: ""
    }

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    init {
        alarmMgr = app.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(app.applicationContext, AlarmReceiver::class.java).let { intent ->
            intent.flags = Intent.FLAG_FROM_BACKGROUND
            PendingIntent.getBroadcast(app.applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        viewModelScope.launch {
            readSettingAlarmFlow.collect {
                savedSettingAlarmTime.value = it
                launch {
                    if (savedSettingAlarmTime.value !== "") {
                        diffAlarmValues.value = diffHoursAndMinutes(parseStringToLocalDateTime(savedSettingAlarmTime.value))
                        diffAlarmTimeText.value = app.applicationContext.getString(
                            R.string.setting_alarm_diff_time_text,
                            diffAlarmValues.value["diffHour"],
                            diffAlarmValues.value["diffMinutes"]
                        )
                        println(diffAlarmValues.value)
                    }
                }
            }
        }
        viewModelScope.launch {
            readSwitchCompatChecked.collect {
                switchCompatChecked.value = it
            }
        }
    }

    fun diffAlarmTime(cal: Calendar, context: Context, hours: Int, minute: Int) {
        viewModelScope.launch {
            context.dataStore.edit { alarm ->
                alarm[PreferenceKeys.settingAlarmHour] = hours
                alarm[PreferenceKeys.settingAlarmMinutes] = minute
                alarm[PreferenceKeys.settingAlarmTextFormatted] = "%02d".format(hours) + ":" + "%02d".format(minute)
            }
        }

        val year = cal.get(Calendar.YEAR)
        val month = formatPadZero((cal.get(Calendar.MONTH) + 1).toString())
        val day = formatPadZero(cal.get(Calendar.DATE).toString())
        val hour = formatPadZero(cal.get(Calendar.HOUR_OF_DAY).toString())
        val minutes = formatPadZero(cal.get(Calendar.MINUTE).toString())

        val jointCal = "$year/$month/$day $hour:$minutes:00"
        val target = parseStringToLocalDateTime(jointCal)
        println(target)
        viewModelScope.launch {
            context.dataStore.edit { alarm ->
                alarm[PreferenceKeys.settingAlarm] = jointCal
            }
        }
        diffAlarmValues.value = diffHoursAndMinutes(target)
        println(diffAlarmValues.value["diffHour"])

        viewModelScope.launch {
            context.dataStore.edit { alarm ->
                alarm[PreferenceKeys.switchChecked] = true
            }
        }
        settingAlarmManager()
    }
    private fun formatPadZero(time: String): String {
        return "%2s".format(time).replace(" ", "0")
    }
    private fun parseStringToLocalDateTime(timeString: String?): LocalDateTime {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        return LocalDateTime.parse(timeString, dtf)
    }
    private fun diffHoursAndMinutes(setTime: LocalDateTime): Map<String, Long> {
        val localDateTime = LocalDateTime.now()
        var diffHour = ChronoUnit.HOURS.between(localDateTime, setTime)
        var diffMinutes = ChronoUnit.MINUTES.between(localDateTime, setTime)
        Log.d("diffHour", diffHour.toString())
        Log.d("diffMinutes", diffMinutes.toString())

        // 　設定した時間が今より前
        if (diffHour < 0) {
            diffMinutes = (diffHour * -1 * 60 - diffMinutes * -1)
            if (diffMinutes < 0) {
                diffMinutes += 60
                diffHour -= 1
            }
            diffHour += 24
            // 設定した時間が今より後
        } else {
            diffMinutes = (diffHour * 60 - diffMinutes) * -1
            // 設定した時間が今と同じで差異が0の時
            if (diffMinutes == 0L && diffHour == 0L) {
                diffMinutes += 0
                diffHour += 24
            }
        }
        if (diffMinutes < 0) {
            diffMinutes += 60
            diffHour -= 1
        }
        if (diffHour < 0) {
            diffHour += 24
        }
        if (diffHour == 0L && diffMinutes < 0) {
            diffMinutes += 60
        }
        return mapOf("diffHour" to diffHour, "diffMinutes" to diffMinutes)
    }
    fun switchClickSwitchToggle(context: Context) {
        switchCompatChecked.value = !switchCompatChecked.value
        viewModelScope.launch {
            context.dataStore.edit { alarm ->
                alarm[PreferenceKeys.switchChecked] = switchCompatChecked.value
            }
        }
        if (!switchCompatChecked.value) {
            alarmMgr?.cancel(alarmIntent)
        } else {
            settingAlarmManager()
        }
    }
    private fun settingAlarmManager() {
        var setHour: Int? = 0
        var setMinute: Int? = 0
        viewModelScope.launch {
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
    suspend fun initAlarmTextShow() {
        settingAlarmTextFormattedFlow.collect {
            mutableAlarmTimeText.value = it
            if (it != null && it.isNotBlank()) {
                mutableAlarmTimeText.value = it
            } else {
                mutableAlarmTimeText.value = null
            }
        }
    }
}
