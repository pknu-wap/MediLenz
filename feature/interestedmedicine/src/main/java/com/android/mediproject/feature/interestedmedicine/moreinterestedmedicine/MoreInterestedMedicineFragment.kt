package com.android.mediproject.feature.interestedmedicine.moreinterestedmedicine

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentMoreInterestedMedicineBinding
import dagger.hilt.android.AndroidEntryPoint
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
            viewModel = fragmentViewModel

            interestedMedicineListRV.apply {
                adapter = moreInterestedMedicineAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MoreInterestedMedcineDecoration(requireContext()))
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
            }
        }

        moreInterestedMedicineAdapter.submitList(
            mutableListOf(
                ApprovedMedicineItemDto(
                    itemName = "탁센연질캡슐",
                    entpName = "(주)녹십자",
                    entpEngName = "GreenSipja",
                    medicineType = "일반의약품",
                    itemIngrName = "나프록센",
                    itemEngName = "Naproxen",
                    itemSeq = 0,
                    entpSeq = "iisque",
                    entpNo = "eius",
                    itemPermitDate = null,
                    induty = null,
                    prdlstStdrCode = null,
                    prductType = "consectetuer",
                    prductPrmisnNo = null,
                    itemIngrCnt = "quaerendum",
                    imgUrl = "http://www.bing.com/search?q=etiam",
                    permitKindCode = null,
                    cancelDate = null,
                    cancelName = null,
                    ediCode = null,
                    bizrno = null,
                    onClick = {},
                ),
                ApprovedMedicineItemDto(
                    itemName = "탁센연질캡슐",
                    entpName = "(주)녹십자",
                    entpEngName = "GreenSipja",
                    medicineType = "일반의약품",
                    itemIngrName = "나프록센",
                    itemEngName = "Naproxen",
                    itemSeq = 0,
                    entpSeq = "iisque",
                    entpNo = "eius",
                    itemPermitDate = null,
                    induty = null,
                    prdlstStdrCode = null,
                    prductType = "consectetuer",
                    prductPrmisnNo = null,
                    itemIngrCnt = "quaerendum",
                    imgUrl = "http://www.bing.com/search?q=etiam",
                    permitKindCode = null,
                    cancelDate = null,
                    cancelName = null,
                    ediCode = null,
                    bizrno = null,
                    onClick = {},
                ),
                ApprovedMedicineItemDto(
                    itemName = "탁센연질캡슐",
                    entpName = "(주)녹십자",
                    entpEngName = "GreenSipja",
                    medicineType = "일반의약품",
                    itemIngrName = "나프록센",
                    itemEngName = "Naproxen",
                    itemSeq = 0,
                    entpSeq = "iisque",
                    entpNo = "eius",
                    itemPermitDate = null,
                    induty = null,
                    prdlstStdrCode = null,
                    prductType = "consectetuer",
                    prductPrmisnNo = null,
                    itemIngrCnt = "quaerendum",
                    imgUrl = "http://www.bing.com/search?q=etiam",
                    permitKindCode = null,
                    cancelDate = null,
                    cancelName = null,
                    ediCode = null,
                    bizrno = null,
                    onClick = {},
                ),
                ApprovedMedicineItemDto(
                    itemName = "탁센연질캡슐",
                    entpName = "(주)녹십자",
                    entpEngName = "GreenSipja",
                    medicineType = "일반의약품",
                    itemIngrName = "나프록센",
                    itemEngName = "Naproxen",
                    itemSeq = 0,
                    entpSeq = "iisque",
                    entpNo = "eius",
                    itemPermitDate = null,
                    induty = null,
                    prdlstStdrCode = null,
                    prductType = "consectetuer",
                    prductPrmisnNo = null,
                    itemIngrCnt = "quaerendum",
                    imgUrl = "http://www.bing.com/search?q=etiam",
                    permitKindCode = null,
                    cancelDate = null,
                    cancelName = null,
                    ediCode = null,
                    bizrno = null,
                    onClick = {},
                ),
                ApprovedMedicineItemDto(
                    itemName = "탁센연질캡슐",
                    entpName = "(주)녹십자",
                    entpEngName = "GreenSipja",
                    medicineType = "일반의약품",
                    itemIngrName = "나프록센",
                    itemEngName = "Naproxen",
                    itemSeq = 0,
                    entpSeq = "iisque",
                    entpNo = "eius",
                    itemPermitDate = null,
                    induty = null,
                    prdlstStdrCode = null,
                    prductType = "consectetuer",
                    prductPrmisnNo = null,
                    itemIngrCnt = "quaerendum",
                    imgUrl = "http://www.bing.com/search?q=etiam",
                    permitKindCode = null,
                    cancelDate = null,
                    cancelName = null,
                    ediCode = null,
                    bizrno = null,
                    onClick = {},
                ),
            )
        )

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