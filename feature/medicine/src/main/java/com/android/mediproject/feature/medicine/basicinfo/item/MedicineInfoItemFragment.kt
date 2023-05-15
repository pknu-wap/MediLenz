package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.text.Html
import android.view.View
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class MedicineInfoItemFragment : BaseMedicineInfoItemFragment<FragmentMedicineInfoItemBinding, MedicineDetatilInfoDto>(
    FragmentMedicineInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override

    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            collectingData.collectLatest {
                val stringBuilder = StringBuilder()
                with(it) {
                    stringBuilder.append("<h1>의약품 정보</h1>").append("<p><b>의약품 이름:</b> $itemName</p>")
                        .append("<p><b>의약품 영문 이름:</b> $itemEnglishName</p>").append("<p><b>의약품 시퀀스 번호:</b> $itemSequence</p>")
                        .append("<p><b>의약품 허가 날짜:</b> $itemPermitDate</p>").append("<p><b>제조사 이름:</b> $entpName</p>")
                        .append("<p><b>제조사 영문 이름:</b> $entpEnglishName</p>").append("<p><b>제조및수입사:</b> $consignmentManufacturer</p>")
                        .append("<p><b>성분 이름:</b> $ingredientName</p>").append("<p><b>주성분의 영문 이름:</b> $mainIngredientEnglish</p>")
                        .append("<p><b>총 함량:</b> $totalContent</p>").append("<p><b>저장 방법:</b> $storageMethod</p>")
                        .append("<p><b>유효 기간:</b> $validTerm</p>").append("<p><b>패키지 단위:</b> $packUnit</p>")
                }

                val htmlText = stringBuilder.toString()
                binding.contentsTextView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            }
        }

    }

}