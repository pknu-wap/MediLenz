package com.android.mediproject.feature.medicine.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.util.SystemBarColorAnalyzer
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.common.viewmodel.onError
import com.android.mediproject.core.common.viewmodel.onLoading
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
    @Inject lateinit var systemBarColorAnalyzer: SystemBarColorAnalyzer

    private val scrollController = ScrollController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.setMedicinePrimaryInfo(navArgs)

        binding.apply {
            systemBarStyler.changeMode(topViews = listOf(SystemBarStyler.ChangeView(topAppBar, SystemBarStyler.SpacingType.PADDING)))
            viewModel = fragmentViewModel
            medicineInfoArgs = navArgs

            toolbar.bar.setBackgroundColor(Color.TRANSPARENT)
            topAppBar.viewTreeObserver.addOnGlobalLayoutListener {
                systemBarColorAnalyzer.convert()
            }

            root.doOnLayout {
                scrollController.init(collapsingToolbarLayout, toolbar, topAppBar)
            }
        }

        viewLifecycleOwner.apply {
            repeatOnStarted {
                fragmentViewModel.medicineDetails.collect {
                    it.onSuccess {
                        initTabs()
                        LoadingDialog.dismiss()
                    }.onError {
                        LoadingDialog.dismiss()
                    }.onLoading {
                        LoadingDialog.showLoadingDialog(requireContext(), null)
                    }
                }
            }

            repeatOnStarted {
                fragmentViewModel.eventState.collectLatest {
                    it.onFavorite { lockChecked, isFavorite ->
                        binding.interestBtn.isEnabled = lockChecked
                        binding.interestBtn.isChecked = isFavorite
                    }
                }
            }

            repeatOnStarted {
                systemBarColorAnalyzer.statusBarColor.collect {
                    (if (it == SystemBarStyler.SystemBarColor.WHITE) Color.WHITE else Color.BLACK).also { color ->
                        binding.toolbar.backButton.imageTintList = ColorStateList.valueOf(color)
                        binding.toolbar.title.setTextColor(color)
                    }
                }
            }
        }
    }


    // 탭 레이아웃 초기화
    private fun initTabs() {
        binding.apply {
            contentViewPager.adapter = MedicineInfoPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            resources.getStringArray(R.array.medicineInfoTab).let { tabTextList ->
                TabLayoutMediator(tabLayout, contentViewPager) { tab, position ->
                    tab.text = tabTextList[position]
                }.attach()
            }

            tabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        if ((tab.position == MedicineInfoViewModel.COMMENT_TAB_POSITION) and tab.isSelected) {
                            scrollController.scrollable = false
                            topAppBar.setExpanded(false, true)
                        } else {
                            scrollController.scrollable = true
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {

                    }

                },
            )
        }

    }

}
