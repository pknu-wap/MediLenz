package com.android.mediproject.feature.interestedmedicine.moreinterestedmedicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentMoreInterestedMedicineBinding

class MoreInterestedMedicineFragment :
    BaseFragment<FragmentMoreInterestedMedicineBinding, MoreInterestedMedicineViewModel>(FragmentMoreInterestedMedicineBinding::inflate) {
    override val fragmentViewModel: MoreInterestedMedicineViewModel by viewModels()
    private val moreInterestedMedicineAdapter: MoreInterestedMeidicneAdapter by lazy { MoreInterestedMeidicneAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel

            interestedMedicineListRV.apply {
                adapter = moreInterestedMedicineAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MoreInterestedMedcineDecoration(requireContext()))
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
            }
        }

        moreInterestedMedicineAdapter.submitList(mutableListOf(
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
        ))

    }

}