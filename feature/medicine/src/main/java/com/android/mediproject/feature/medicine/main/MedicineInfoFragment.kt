package com.android.mediproject.feature.medicine.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.android.mediproject.core.common.dialog.showLoadingDialog
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

    private var dialog: AlertDialog? = null

    private val nagArgs: MedicineInfoArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = fragmentViewModel

        binding.apply {
            viewModel = fragmentViewModel
            // smoothly hide medicinePrimaryInfoViewgroup when collapsing toolbar
            topAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                log("verticalOffset : $verticalOffset, appBarLayout.totalScrollRange : ${appBarLayout.totalScrollRange}")
                medicinePrimaryInfoViewgroup.alpha = 1.0f + (verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat()).apply {
                    if (this == -1.0f) {
                        medicinePrimaryInfoViewgroup.visibility = View.INVISIBLE
                    } else if (this > -0.8f) {
                        medicinePrimaryInfoViewgroup.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.medicineDetails.collect {
                when (it) {
                    is UiState.Success -> {
                        initTabs()
                        dialog?.dismiss()
                    }

                    is UiState.Error -> {

                    }

                    is UiState.Loading -> {
                        fragmentViewModel.medicineName.value.let { medicineName ->
                            val msg = "$medicineName ${getString(R.string.loadingMedicineDetails)}"
                            dialog = showLoadingDialog(requireActivity(), msg)
                        }
                    }

                    is UiState.Initial -> {

                    }

                }
            }
        }

        nagArgs.apply {
            fragmentViewModel.setMedicinePrimaryInfo(this)
            fragmentViewModel.loadMedicineDetails(medicineName)
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