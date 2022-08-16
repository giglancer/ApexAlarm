package com.apexalramapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apexalramapp.R
import com.apexalramapp.adapter.WeaponTypeListAdapter
import com.apexalramapp.data.WeaponType
import com.apexalramapp.databinding.FragmentWeaponTypeSelectionBinding
import com.apexalramapp.viewModel.WeaponTypeSelectionFragmentViewModel

class WeaponTypeSelectionFragment : Fragment() {
    private val weaponTypeViewModel: WeaponTypeSelectionFragmentViewModel by viewModels()
    lateinit var binding: FragmentWeaponTypeSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weapon_type_selection, container, false)
        binding.viewModel = weaponTypeViewModel
        binding.lifecycleOwner = this

        binding.weaponTypeList.adapter = object : WeaponTypeListAdapter() {
            override val clickListener: (weaponType: WeaponType) -> Unit = {
                Log.d("Clicked", requireContext().getString(it.type))
                findNavController().navigate(
                    WeaponTypeSelectionFragmentDirections
                        .actionWeaponTypeSelectionFragmentToWeaponDetailSelectionFragment(it.type)
                )
            }
        }
        return binding.root
    }
}
