package com.android.mediproject.feature.medicine.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.uiutil.SystemBarColorAnalyzer
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted
import javax.inject.Inject

/**
 * 약 정보 화면
 *
 */
@AndroidEntryPoint
class MedicineInfoFragment : BaseFragment<FragmentMedicineInfoBinding, MedicineInfoViewModel>(FragmentMedicineInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels()

    private val navArgs by navArgs<MedicineInfoArgs>()

    @Inject lateinit var systemBarStyler: SystemBarController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.setMedicinePrimaryInfo(navArgs)
        setBarStyle()

        binding.apply {
            systemBarStyler.changeMode(topViews = listOf(SystemBarStyler.ChangeView(topAppBar, SystemBarStyler.SpacingType.PADDING)))
            viewModel = fragmentViewModel
            medicineInfoArgs = navArgs

            toolbar.bar.setBackgroundColor(Color.TRANSPARENT)
            topAppBar.addOnOffsetChangedListener { _, _ ->
                SystemBarColorAnalyzer.convert()
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            launch {
                fragmentViewModel.medicineDetails.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            initTabs()
                            LoadingDialog.dismiss()
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismiss()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showLoadingDialog(requireContext(), null)
                        }

                        is UiState.Initial -> {}

                    }
                }
            }

            launch {
                fragmentViewModel.eventState.collectLatest {
                    when (it) {
                        is EventState.Favorite -> {
                            binding.interestBtn.isEnabled = it.lockChecked
                            binding.interestBtn.isChecked = it.isFavorite
                        }

                        is EventState.ScrollToBottom -> {
                            binding.topAppBar.setExpanded(false, true)
                        }
                    }
                }

            }

            launch {
                SystemBarColorAnalyzer.statusBarColor.collect {
                    (if (it == SystemBarStyler.SystemBarColor.WHITE) Color.WHITE else Color.BLACK).also { color ->
                        binding.toolbar.backButton.imageTintList =
                            ColorStateList.valueOf(color)
                        binding.toolbar.title.setTextColor(color)
                    }
                }
            }
        }
    }

    private fun setBarStyle() = binding.apply {

    }


    // 탭 레이아웃 초기화
    private fun initTabs() {
        binding.apply {
            contentViewPager.adapter = MedicineInfoPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            // 탭 레이아웃에 탭 추가
            resources.getStringArray(R.array.medicineInfoTab).also { tabTextList ->
                TabLayoutMediator(tabLayout, contentViewPager) { tab, position ->
                    tab.text = tabTextList[position]
                }.attach()
            }
        }

    }


}
