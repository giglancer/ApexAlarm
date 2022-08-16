package com.apexalramapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apexalramapp.data.CreateWeaponList
import com.apexalramapp.data.WeaponInfo
import com.apexalramapp.data.WeaponType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeaponTypeSelectionFragmentViewModel: ViewModel() {
    private val _property : MutableStateFlow<List<WeaponType>> = MutableStateFlow<List<WeaponType>>(CreateWeaponList.weaponTypeList())
    val properties : List<WeaponType> = _property.value
}