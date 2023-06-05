package com.android.mediproject.feature.medicine.main

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

/**
 * 약 정보 화면
 *
 */
@AndroidEntryPoint
class MedicineInfoFragment : BaseFragment<FragmentMedicineInfoBinding, MedicineInfoViewModel>(FragmentMedicineInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels()

    private val navArgs by navArgs<MedicineInfoArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel.setMedicinePrimaryInfo(navArgs)

        binding.apply {

            viewModel = fragmentViewModel
            medicineInfoArgs = navArgs

            root.doOnPreDraw {
                /**
                // coordinatorlayout으로 인해 viewpager의 높이가 휴대폰 화면 하단을 벗어나 버리는 현상을 방지하기 위해 사용
                val viewPagerHeight = root.height - topAppBar.height
                contentViewPager.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewPagerHeight).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
                }
                 */

                topAppBar.removeOnOffsetChangedListener(null)
                // smoothly hide medicinePrimaryInfoViewgroup when collapsing toolbar
                topAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                    // 스크롤 할 때 마다 medicinePrimaryInfoViewgroup의 투명도 조정
                    medicinePrimaryInfoViewgroup.alpha = 1.0f + (verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat()).apply {
                        if (this == -1.0f) medicinePrimaryInfoViewgroup.visibility = View.INVISIBLE
                        else if (this > -0.8f) medicinePrimaryInfoViewgroup.isVisible = true
                    }

                    /**
                    // 스크롤 할 때 마다 viewpager의 높이를 조정
                    contentViewPager.layoutParams =
                    CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (viewPagerHeight - verticalOffset)).apply {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                    }
                     */
                }

            }
        }

        viewLifecycleOwner.repeatOnStarted {
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
                        fragmentViewModel.medicinePrimaryInfo.replayCache.first().let {
                            LoadingDialog.showLoadingDialog(requireContext(),
                                "it.itemKorName ${getString(R.string.loadingMedicineDetails)}")
                        }
                    }

                    is UiState.Initial -> {}

                }
            }
        }

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