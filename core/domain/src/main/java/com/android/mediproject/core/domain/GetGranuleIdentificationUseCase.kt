package com.android.mediproject.core.domain

import android.content.Context
import android.text.Html
import android.text.Spanned
import androidx.core.text.toSpanned
import com.android.mediproject.core.common.R
import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepository
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.core.model.remote.granule.toDto
import javax.inject.Inject

class GetGranuleIdentificationUseCase @Inject constructor(
    private val repository: GranuleIdentificationRepository
) {


    /**
     * 약품 식별 정보 조회
     *
     * @param itemName 약품명
     * @param entpName 업체명
     * @param itemSeq 약품 고유 번호
     */
    suspend operator fun invoke(
        itemName: String?, entpName: String?, itemSeq: String?
    ): Result<GranuleIdentificationInfoDto> =
        repository.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).map {
            it.toDto()
        }


    suspend fun createDataTags(context: Context, granuleDto: GranuleIdentificationInfoDto): Spanned =
        mutableMapOf<String, List<Pair<String, String>>>().apply {
            // 그룹 1: 의약품 정보
            context.resources.getStringArray(R.array.medicineInfo).also {
                this[context.getString(R.string.medicineInfoTitle)] = listOf(
                    it[0] to granuleDto.itemSeq, it[1] to granuleDto.itemName, it[2] to granuleDto.itemEngName
                )
            }

            // 그룹 2: 업체 정보
            context.resources.getStringArray(R.array.companyInfo).also {
                this[context.getString(R.string.companyInfoTitle)] = listOf(
                    it[0] to granuleDto.entpSeq, it[1] to granuleDto.entpName, it[2] to granuleDto.bizrNo
                )
            }

            // 그룹 3: 의약품 분류 정보
            context.resources.getStringArray(R.array.classificationInfo).also {
                this[context.getString(R.string.classificationInfoTitle)] = listOf(
                    it[0] to granuleDto.classNo, it[1] to granuleDto.className, it[2] to granuleDto.etcOtcName
                )
            }

            // 그룹 4: 기타 정보
            context.resources.getStringArray(R.array.miscInfo).also {
                this[context.getString(R.string.miscInfoTitle)] = listOf(
                    it[0] to granuleDto.formCodeName,
                    it[1] to granuleDto.itemPermitDate.toString(),
                    it[2] to granuleDto.changeDate.toString()
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
                    it[5] to granuleDto.markCodeBackImg
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
        }.toMap().let { dataMap ->
            val builder = StringBuilder()
            dataMap.forEach { (groupTitle, items) ->
                builder.append("<h2>").append(groupTitle).append("</h2>")
                items.forEach { pair ->
                    builder.append("<p><b>").append(pair.first).append(": </b>").append(pair.second).append("</p>")
                }
                builder.append("<br>")
            }
            Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT).toSpanned()
        }
}