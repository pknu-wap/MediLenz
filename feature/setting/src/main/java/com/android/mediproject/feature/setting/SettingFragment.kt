package com.android.mediproject.feature.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.setting.databinding.FragmentSettingBinding
import repeatOnStarted

class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(FragmentSettingBinding::inflate) {
    override val fragmentViewModel: SettingViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }

    fun handleEvent(event: SettingViewModel.SettingEvent) {
        val intent = Intent(Intent.ACTION_VIEW)
        when (event) {
            is SettingViewModel.SettingEvent.Notice -> intent.setData(Uri.parse("https://www.notion.so/c4f400e9e9ed46b19a20375028c3a0df"))
            is SettingViewModel.SettingEvent.Privacy -> intent.setData(Uri.parse("https://www.notion.so/95b9e085523b4a21ae2624f7813bf5d1"))
            is SettingViewModel.SettingEvent.Policy -> intent.setData(Uri.parse("https://www.notion.so/78f48ae1e85942c2bda087529717ca91"))
            is SettingViewModel.SettingEvent.Communicate -> intent.setData(Uri.parse("https://www.notion.so/b8f23da7037d4f148e83a4c464b6b88c"))
            is SettingViewModel.SettingEvent.Introduce -> intent.setData(Uri.parse("https://www.notion.so/e689497e3321452ab9826768a038681c"))
        }
        startActivity(intent)
    }
}