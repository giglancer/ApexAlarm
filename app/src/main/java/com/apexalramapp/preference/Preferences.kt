package com.apexalramapp.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStore {
    val Context.mediaDataStore: DataStore<Preferences> by preferencesDataStore(name = "player")
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "alarm")
}
object PreferenceKeys {
    // alarm
    val settingAlarm = stringPreferencesKey("settingAlarm")
    val settingAlarmHour = intPreferencesKey("settingAlarmHour")
    val settingAlarmMinutes = intPreferencesKey("settingAlarmMinutes")
    val settingAlarmTextFormatted = stringPreferencesKey("settingAlarmTextFormatted")
    val switchChecked = booleanPreferencesKey("switchChecked")

    // player
    val mediaPlayFlg = booleanPreferencesKey("mediaPlayFlg")
    val settingMedia = intPreferencesKey("settingMedia")
}
