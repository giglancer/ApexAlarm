package com.apexalramapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apexalramapp.preference.DataStore.mediaDataStore
import com.apexalramapp.preference.PreferenceKeys
import com.apexalramapp.R
import com.apexalramapp.adapter.WeaponDetailListAdapter
import com.apexalramapp.data.CreateWeaponList
import com.apexalramapp.data.Weapon
import com.apexalramapp.databinding.FragmentWeaponDetailSelectionBinding
import com.apexalramapp.viewModel.WeaponDetailSelectionFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeaponDetailSelectionFragment : Fragment() {
    private val weaponDetailViewModel: WeaponDetailSelectionFragmentViewModel by viewModels()
    lateinit var binding: FragmentWeaponDetailSelectionBinding

    private val args: WeaponDetailSelectionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weaponDetailViewModel.property.value = when (args.weaponType) {
            R.string.weapon_ar -> CreateWeaponList.arList()
            R.string.weapon_lmg -> CreateWeaponList.lmgList()
            R.string.weapon_pistol -> CreateWeaponList.pistolList()
            R.string.weapon_shotgun -> CreateWeaponList.shotgunList()
            R.string.weapon_marksman -> CreateWeaponList.marksmanList()
            R.string.weapon_sniper_rifle -> CreateWeaponList.sniperRifleList()
            R.string.weapon_smg -> CreateWeaponList.smgList()
            else -> emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weapon_detail_selection, container, false)
        binding.viewModel = weaponDetailViewModel
        binding.lifecycleOwner = this

        Log.d("リスト", weaponDetailViewModel.property.value.toString())

        val readMediaPlayFlg: Flow<Int> = requireContext().applicationContext.mediaDataStore.data.map {
                pref ->
            pref[PreferenceKeys.settingMedia] ?: 0
        }

        binding.weaponDetailList.adapter = object : WeaponDetailListAdapter() {
            override val weaponDetailClickListener: (weapon: Weapon) -> Unit = {
                Log.d("Clicked", requireContext().getString(it.name))
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        readMediaPlayFlg.collect { settingMedia ->
                            if (it.gunShot == settingMedia) {
                                requireContext().mediaDataStore.edit { media ->
                                    media[PreferenceKeys.mediaPlayFlg] = false
                                }
                                Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.weapon_select_correct, Snackbar.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_weaponDetailSelectionFragment_to_alarmSettingFragment)
                            } else {
                                Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.weapon_select_incorrect, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }
}
