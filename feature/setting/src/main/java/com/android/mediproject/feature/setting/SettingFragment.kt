package com.android.mediproject.feature.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.setting.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(FragmentSettingBinding::inflate) {
    override val fragmentViewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.BLACK, SystemBarStyler.NavigationBarColor.BLACK)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
            systemBarStyler.changeMode(topViews = listOf(SystemBarStyler.ChangeView(settingBar, SystemBarStyler.SpacingType.PADDING)))
        }
    }

    private fun handleEvent(event: SettingViewModel.SettingEvent) {
        val intent = Intent(Intent.ACTION_VIEW)
        when (event) {
            is SettingViewModel.SettingEvent.Notice -> intent.data = "https://www.notion.so/c4f400e9e9ed46b19a20375028c3a0df".toUri()
            is SettingViewModel.SettingEvent.Privacy -> intent.data = "https://www.notion.so/95b9e085523b4a21ae2624f7813bf5d1".toUri()
            is SettingViewModel.SettingEvent.Policy -> intent.data = "https://www.notion.so/78f48ae1e85942c2bda087529717ca91".toUri()
            is SettingViewModel.SettingEvent.Communicate -> intent.data = "https://www.notion.so/b8f23da7037d4f148e83a4c464b6b88c".toUri()
            is SettingViewModel.SettingEvent.Introduce -> intent.data = "https://www.notion.so/e689497e3321452ab9826768a038681c".toUri()
        }
        startActivity(intent)
    }
}