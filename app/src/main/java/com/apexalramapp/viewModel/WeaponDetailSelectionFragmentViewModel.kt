package com.apexalramapp.viewModel

import androidx.lifecycle.ViewModel
import com.apexalramapp.data.CreateWeaponList
import com.apexalramapp.data.Weapon
import com.apexalramapp.data.WeaponType
import kotlinx.coroutines.flow.MutableStateFlow

class WeaponDetailSelectionFragmentViewModel : ViewModel() {
    var property : MutableStateFlow<List<Weapon>> = MutableStateFlow<List<Weapon>>(emptyList())
    var properties : List<Weapon> = property.value
}