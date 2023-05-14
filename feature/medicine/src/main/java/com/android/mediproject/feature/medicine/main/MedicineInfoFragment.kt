package com.android.mediproject.feature.medicine.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.android.mediproject.core.common.dialog.ProgressDialog
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
        initTabs()

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.medicineDetails.collect {
                when (it) {
                    is UiState.Success -> {
                        binding.tabLayout.getTabAt(0)?.select()
                        dialog?.dismiss()
                    }

                    is UiState.Error -> {

                    }

                    is UiState.Loading -> {
                        fragmentViewModel.medicineName.value.let { medicineName ->
                            val msg = "$medicineName ${getString(R.string.loadingMedicineDetails)}"
                            dialog = ProgressDialog.createDialog(requireActivity(), msg).also { dialog ->
                                dialog.show()
                            }
                        }
                    }

                    is UiState.Initial -> {

                    }

                }
            }
        }
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        nagArgs.apply {
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
                    when (position) {
                        0 -> {
                            tab.text = tabTextList[0]
                        }

                        1 -> {
                            tab.text = tabTextList[1]
                        }

                        2 -> {
                            tab.text = tabTextList[2]
                        }

                        3 -> {
                            tab.text = tabTextList[3]
                        }
                    }
                }.attach()
            }
        }

    }


}