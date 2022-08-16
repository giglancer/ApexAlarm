package com.apexalramapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.apexalramapp.R
import com.apexalramapp.databinding.FragmentAlarmSettingBinding
import com.apexalramapp.preference.DataStore.mediaDataStore
import com.apexalramapp.preference.PreferenceKeys
import com.apexalramapp.utils.CHANNEL_ID
import com.apexalramapp.utils.Notification
import com.apexalramapp.viewModel.AlarmSettingFragmentViewModel
import com.apexalramapp.viewModelFactory.AlarmSettingViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AlarmSettingFragment : Fragment() {

    lateinit var alarmSettingFragmentViewModel: AlarmSettingFragmentViewModel

    lateinit var binding: FragmentAlarmSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentAlarmSettingBinding>(
            inflater,
            R.layout.fragment_alarm_setting,
            container,
            false
        )
        val application = requireNotNull(activity).application
        val viewModelFactory = AlarmSettingViewModelFactory(application)
        alarmSettingFragmentViewModel = ViewModelProvider(this, viewModelFactory)[AlarmSettingFragmentViewModel::class.java]
        binding.viewModel = alarmSettingFragmentViewModel
        binding.lifecycleOwner = this

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                alarmSettingFragmentViewModel.initAlarmTextShow()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Notification.notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(requireContext().getString(CHANNEL_ID), getString(R.string.notification_channel_name))

        binding.addAlarm.setOnClickListener {
            showTimePickerDialog()
        }

        val readMediaPlayFlg: Flow<Boolean?> = requireContext().applicationContext.mediaDataStore.data.map {
                pref ->
            pref[PreferenceKeys.mediaPlayFlg] ?: false
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                readMediaPlayFlg.collect {
                    if (it == true) {
                        findNavController().navigate(R.id.action_alarmSettingFragment_to_weaponTypeSelectionFragment)
                    }
                }
            }
        }
    }
    private fun showTimePickerDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            Log.d("calender", hour.toString())
            Log.d("calender", minute.toString())

            alarmSettingFragmentViewModel.mutableAlarmTimeText.value = SimpleDateFormat("HH:mm", Locale.JAPANESE).format(cal.time)
            alarmSettingFragmentViewModel.diffAlarmTime(cal, requireContext(), hour, minute)

            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.setting_alarm_setting_completed, Snackbar.LENGTH_SHORT).show()
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = getString(R.string.notification_channel_description)
        }
        Notification.notificationManager.createNotificationChannel(channel)
    }
}
