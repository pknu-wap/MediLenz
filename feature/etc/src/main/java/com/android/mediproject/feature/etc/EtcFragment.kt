package com.android.mediproject.feature.etc

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.etc.databinding.FragmentEtcBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class EtcFragment :
    BaseFragment<FragmentEtcBinding, EtcViewModel>(FragmentEtcBinding::inflate) {
    override val fragmentViewModel: EtcViewModel by viewModels()

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            repeatOnStarted { eventFlow.collect { handleEvent(it) } }
        }
        setBarStyle()
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(topViews = listOf(SystemBarStyler.ChangeView(etcBar, SystemBarStyler.SpacingType.PADDING)))
    }

    private fun handleEvent(event: EtcViewModel.EtcEvent) {
        when (event) {
            is EtcViewModel.EtcEvent.Notice -> openWebWithUri("https://www.notion.so/c4f400e9e9ed46b19a20375028c3a0df".toUri())
            is EtcViewModel.EtcEvent.Privacy -> openWebWithUri("https://www.notion.so/95b9e085523b4a21ae2624f7813bf5d1".toUri())
            is EtcViewModel.EtcEvent.Policy -> openWebWithUri("https://www.notion.so/78f48ae1e85942c2bda087529717ca91".toUri())
            is EtcViewModel.EtcEvent.Communicate -> openWebWithUri("https://www.notion.so/b8f23da7037d4f148e83a4c464b6b88c".toUri())
            is EtcViewModel.EtcEvent.Introduce -> openWebWithUri("https://www.notion.so/e689497e3321452ab9826768a038681c".toUri())
        }
    }

    private fun openWebWithUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        startActivity(intent)
    }
}
