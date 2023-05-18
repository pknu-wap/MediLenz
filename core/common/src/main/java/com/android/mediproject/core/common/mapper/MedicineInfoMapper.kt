package com.android.mediproject.core.common.mapper

import android.text.Html
import android.text.Spanned
import androidx.core.text.toSpanned
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.util.XMLParsedResult
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 의약품 정보 데이터를 UI에 맞게 변환하는 Mapper
 *
 * 용법용량 정보 데이터를 변환
 */
@Singleton
class MedicineInfoMapper @Inject constructor() {
    /**
     * 용법용량 정보 데이터를 UI에 맞게 변환
     */
    fun toDosageInfo(xmlParsedResult: XMLParsedResult): Spanned {
        val stringBuilder = WeakReference(StringBuilder())

        return stringBuilder.get()?.let { builder ->
            xmlParsedResult.articleList.forEach { article ->
                builder.append("<b>$article.title</b><br>")
                builder.append(article.contentList)
                builder.append("<br>>")
            }

            val result = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
            builder.clear()
            stringBuilder.clear()

            result
        } ?: "".toSpanned()
    }

    /**
     * 효능효과 데이터를 UI에 맞게 변환
     */
    fun toEfficacyEffect(xmlParsedResult: XMLParsedResult): Spanned {
        val stringBuilder = WeakReference(StringBuilder())

        return stringBuilder.get()?.let { builder ->
            xmlParsedResult.articleList.forEach { article ->
                builder.append(article.contentList)
            }

            val result = builder.toSpanned()
            builder.clear()
            stringBuilder.clear()
            result
        } ?: "".toSpanned()
    }

    /**
     * 의약품 기본 정보 데이터를 UI에 맞게 변환
     */
    fun toMedicineInfo(medicineDetatilInfoDto: MedicineDetatilInfoDto): Spanned {
        val stringBuilder = WeakReference(StringBuilder())

        return stringBuilder.get()?.let { builder ->
            with(medicineDetatilInfoDto) {
                builder.append("<p><b>의약품 이름:</b> $itemName</p>").append("<p><b>의약품 영문 이름:</b> $itemEnglishName</p>")
                    .append("<p><b>의약품 시퀀스 번호:</b> $itemSequence</p>").append("<p><b>의약품 허가 날짜:</b> $itemPermitDate</p>")
                    .append("<p><b>제조사 이름:</b> $entpName</p>").append("<p><b>제조사 영문 이름:</b> $entpEnglishName</p>")
                    .append("<p><b>제조및수입사:</b> $consignmentManufacturer</p>").append("<p><b>성분 이름:</b> $ingredientName</p>")
                    .append("<p><b>주성분의 영문 이름:</b> $mainIngredientEnglish</p>").append("<p><b>총 함량:</b> $totalContent</p>")
                    .append("<p><b>저장 방법:</b> $storageMethod</p>").append("<p><b>유효 기간:</b> $validTerm</p>")
                    .append("<p><b>패키지 단위:</b> $packUnit</p>")
            }

            val result = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
            builder.clear()
            stringBuilder.clear()
            result
        } ?: "".toSpanned()
    }
}