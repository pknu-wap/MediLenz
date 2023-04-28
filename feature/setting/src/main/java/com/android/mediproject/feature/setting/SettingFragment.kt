package com.android.mediproject.feature.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.setting.databinding.FragmentSettingBinding
import repeatOnStarted

class SettingFragment : BaseFragment<FragmentSettingBinding,SettingViewModel>(FragmentSettingBinding::inflate) {
    override val fragmentViewModel: SettingViewModel by viewModels()

    override fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding.apply{
            viewModel = fragmentViewModel.apply{
                repeatOnStarted { eventFlow.collect{ handelEvent(it)} }
            }
        }
    }

    fun handelEvent(event : SettingViewModel.SettingEvent) = when(event){
        else -> {}
    }
}