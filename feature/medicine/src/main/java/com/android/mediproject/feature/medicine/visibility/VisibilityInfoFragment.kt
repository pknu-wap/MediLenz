package com.android.mediproject.feature.medicine.visibility

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.dialog.showLoadingDialog
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentVisibilityInfoBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted

/**
 * 해당 화면은 의약품 식별 정보(낱알 정보, 형태 등)를 보여주는 화면입니다.
 *
 * 낱알 이미지, 형태(정제, 캡슐, 액제 등), 색상, 크기 등을 표시합니다.
 *
 * 상세 정보를 보기 위한 또는 버튼이 제공됩니다.
 */

@AndroidEntryPoint
class VisibilityInfoFragment :
    BaseFragment<FragmentVisibilityInfoBinding, VisibilityInfoViewModel>(FragmentVisibilityInfoBinding::inflate) {

    override val fragmentViewModel: VisibilityInfoViewModel by viewModels()

    private val medicineInfoViewModel by viewModels<MedicineInfoViewModel>({
        requireParentFragment()
    })

    private var dialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.repeatOnStarted {

            launch {
                fragmentViewModel.granuleTextTags.collect {
                    binding.medicineVisibilityInfoView.text = it
                    dialog?.dismiss()
                }
            }

            launch {

                fragmentViewModel.granuleIdentification.collect {
                    when (it) {
                        is UiState.Success -> {
                            fragmentViewModel.createDataTags(requireContext())
                        }

                        else -> {}
                    }
                }
            }

            launch {
                medicineInfoViewModel.medicineDetails.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            fragmentViewModel.getGranuleIdentificationInfo(
                                itemName = null, entpName = null, itemSeq = it.data.itemSequence
                            )
                        }

                        else -> {}
                    }
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        dialog = showLoadingDialog(requireActivity(), null)
    }

}