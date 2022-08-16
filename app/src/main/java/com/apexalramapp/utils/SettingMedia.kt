package com.apexalramapp.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.apexalramapp.R
import com.apexalramapp.data.CreateWeaponList
import com.apexalramapp.preference.DataStore.mediaDataStore
import com.apexalramapp.preference.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object SettingMedia {
    fun setMedia(context: Context): Int {
        val scope = CoroutineScope(Job())

        val type = CreateWeaponList.weaponTypeList().shuffled()[0]
        Log.d("setMedia_type", type.type.toString())
        var weapon = when (type.type) {
            R.string.weapon_ar -> CreateWeaponList.arList()
            R.string.weapon_lmg -> CreateWeaponList.lmgList()
            R.string.weapon_pistol -> CreateWeaponList.pistolList()
            R.string.weapon_shotgun -> CreateWeaponList.shotgunList()
            R.string.weapon_marksman -> CreateWeaponList.marksmanList()
            R.string.weapon_sniper_rifle -> CreateWeaponList.sniperRifleList()
            R.string.weapon_smg -> CreateWeaponList.smgList()
            else -> CreateWeaponList.arList()
        }
        val shuffledWeapon = weapon.shuffled()[0].gunShot
        scope.launch {
            context.mediaDataStore.edit {
                it[PreferenceKeys.settingMedia] = shuffledWeapon
                Log.d("shuffledWeapon", shuffledWeapon.toString())
            }
        }
        Log.d("shuffledWeapon_return", shuffledWeapon.toString())
        return shuffledWeapon
    }
}
