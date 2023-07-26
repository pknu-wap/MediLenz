package com.android.mediproject.core.common.mapper

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import androidx.core.text.toSpanned
import com.android.mediproject.core.common.R
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfo
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
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

    companion object {
        private const val TEXT_SIZE_PERCENT = 1.2F
    }

    /**
     * 용법용량 정보 데이터를 UI에 맞게 변환
     */
    fun toDosageInfo(xmlParsedResult: XMLParsedResult): Spanned {
        val stringBuilder = WeakReference(StringBuilder())

        return stringBuilder.get()?.let { builder ->
            xmlParsedResult.articleList.forEach { article ->
                builder.append("<b>${article.title}</b><br>")
                article.contentList.forEach { content ->
                    builder.append("$content<br>")
                }
                builder.append("<br>")
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
                article.contentList.forEach { content ->
                    builder.append("$content<br>")
                }
                builder.append("<br>")
            }

            val result = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
            builder.clear()
            stringBuilder.clear()
            result
        } ?: "".toSpanned()
    }

    /**
     * 의약품 기본 정보 데이터를 UI에 맞게 변환
     */
    fun toMedicineInfo(medicineDetailInfo: MedicineDetailInfo): Spanned {
        val stringBuilder = WeakReference(StringBuilder())

        return stringBuilder.get()?.let { builder ->
            with(medicineDetailInfo) {
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


    suspend fun toGranuleInfo(context: Context, granuleDto: GranuleIdentificationInfoDto): Spanned =
        mutableMapOf<String, List<Pair<String, String>>>().apply {
            // 그룹 1: 의약품 정보
            context.resources.getStringArray(R.array.medicineInfo).also {
                this[context.getString(R.string.medicineInfoTitle)] = listOf(
                    it[0] to granuleDto.itemSeq, it[1] to granuleDto.itemName, it[2] to granuleDto.itemEngName,
                )
            }

            // 그룹 2: 업체 정보
            context.resources.getStringArray(R.array.companyInfo).also {
                this[context.getString(R.string.companyInfoTitle)] = listOf(
                    it[0] to granuleDto.entpSeq, it[1] to granuleDto.entpName, it[2] to granuleDto.bizrNo,
                )
            }

            // 그룹 3: 의약품 분류 정보
            context.resources.getStringArray(R.array.classificationInfo).also {
                this[context.getString(R.string.classificationInfoTitle)] = listOf(
                    it[0] to granuleDto.classNo, it[1] to granuleDto.className, it[2] to granuleDto.etcOtcName,
                )
            }

            // 그룹 4: 기타 정보
            context.resources.getStringArray(R.array.miscInfo).also {
                this[context.getString(R.string.miscInfoTitle)] = listOf(
                    it[0] to granuleDto.formCodeName,
                    it[1] to granuleDto.itemPermitDate.toString(),
                    it[2] to granuleDto.changeDate.toString(),
                )
            }

            // 그룹 5: 인쇄 및 이미지 정보
            context.resources.getStringArray(R.array.printAndImageInfo).also {
                this[context.getString(R.string.printAndImageInfoTitle)] = listOf(
                    it[0] to (granuleDto.printFront ?: "없음"),
                    it[1] to (granuleDto.printBack ?: "없음"),
                    it[2] to granuleDto.markCodeFrontAnal,
                    it[3] to granuleDto.markCodeBackAnal,
                    it[4] to granuleDto.markCodeFrontImg,
                    it[5] to granuleDto.markCodeBackImg,
                )
            }

            // 그룹 6: 의약품 이미지 정보
            context.resources.getStringArray(R.array.medicineImageInfo).also {
                this[context.getString(R.string.medicineImageInfoTitle)] = listOf(
                    it[0] to granuleDto.chart,
                    it[1] to granuleDto.itemImage,
                )
            }

            // 그룹 7: 의약품 형태 정보
            context.resources.getStringArray(R.array.medicineShapeInfo).also {
                this[context.getString(R.string.medicineShapeInfoTitle)] = listOf(
                    it[0] to granuleDto.drugShape,
                    it[1] to granuleDto.colorClass1,
                    it[2] to (granuleDto.colorClass2 ?: "없음"),
                )
            }

            // 그룹 8: 구분선 정보
            context.resources.getStringArray(R.array.lineInfo).also {
                this[context.getString(R.string.lineInfoTitle)] = listOf(
                    it[0] to (granuleDto.lineFront ?: "없음"),
                    it[1] to (granuleDto.lineBack ?: "없음"),
                    it[2] to granuleDto.lengLong,
                    it[3] to granuleDto.lengShort,
                    it[4] to (granuleDto.thick ?: "없음"),
                    it[5] to granuleDto.imgRegistTs.toString(),
                )
            }
        }.let { dataMap ->
            val builder = WeakReference(StringBuilder())
            val result = builder.get()?.let { builder ->
                dataMap.forEach { (title, data) ->
                    builder.append("<p><b>$title</b></p>")
                    data.forEach { (key, value) ->
                        builder.append("<p><b>$key:</b> $value</p>")
                    }
                }
                Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT).toSpanned()
            } ?: "".toSpanned()

            builder.clear()
            dataMap.clear()
            result
        }

    fun initHeaderSpan(context: Context, text: String): SpannableStringBuilder {
        return SpannableStringBuilder(text).apply {
            val underline1Idx =
                text.indexOf(context.getString(R.string.highlightWord1)) to text.indexOf(context.getString(R.string.highlightWord1)) + 2

            setSpan(UnderlineSpan(), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(RelativeSizeSpan(TEXT_SIZE_PERCENT), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            val underline2Idx =
                text.indexOf(context.getString(R.string.highlightWord2)) to text.indexOf(context.getString(R.string.highlightWord2)) + 2

            setSpan(UnderlineSpan(), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(RelativeSizeSpan(TEXT_SIZE_PERCENT), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    fun getNoHistorySpan(context: Context): SpannableStringBuilder {
        val text = context.getString(R.string.failedLoading)
        val highLightIndex = text.indexOf(context.getString(R.string.highlightWord3))
        return SpannableStringBuilder(text).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.main)),
                highLightIndex,
                highLightIndex + 3,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), highLightIndex, highLightIndex + 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
