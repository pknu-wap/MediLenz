package com.android.mediproject.feature.interestedmedicine.moreinterestedmedicine

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineDto
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentMoreInterestedMedicineBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MoreInterestedMedicineFragment :
    BaseFragment<FragmentMoreInterestedMedicineBinding, MoreInterestedMedicineViewModel>(
        FragmentMoreInterestedMedicineBinding::inflate
    ) {
    override val fragmentViewModel: MoreInterestedMedicineViewModel by viewModels()
    private val moreInterestedMedicineAdapter: MoreInterestedMeidicneAdapter by lazy { MoreInterestedMeidicneAdapter() }

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(
            SystemBarStyler.StatusBarColor.BLACK,
            SystemBarStyler.NavigationBarColor.BLACK
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBarStyle()
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted {
                        interstedMedicineList.collect {
                            setInterstedMedicineList(it)
                        }
                    }
                    repeatOnStarted {
                        token.collect {
                            handleToken(it)
                        }
                    }
                }
            }

            interestedMedicineListRV.apply {
                adapter = moreInterestedMedicineAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MoreInterestedMedcineDecoration(requireContext()))
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
            }
        }
    }

    private fun setInterstedMedicineList(medicineList: List<InterestedMedicineDto>) {
        moreInterestedMedicineAdapter.submitList(medicineList)
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    interestedMedicineListBar,
                    SystemBarStyler.SpacingType.PADDING
                )
            )
        )
    }

}