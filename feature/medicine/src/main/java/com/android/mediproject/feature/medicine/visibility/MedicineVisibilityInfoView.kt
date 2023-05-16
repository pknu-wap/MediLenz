package com.android.mediproject.feature.medicine.visibility

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.android.mediproject.core.common.uiutil.dpToPx
import com.android.mediproject.core.common.uiutil.spToPx
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.feature.medicine.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * 의약품 식별 정보(낱알 정보, 형태 등)를 화면에 보여줄때 사용하는 TextView 입니다.
 */
class MedicineVisibilityInfoView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val labelTextPaint = TextPaint().apply {
        textSize = spToPx(context, 14).toFloat()
        color = Color.Black.toArgb()
        isFakeBoldText = true
    }

    private val valueTextPaint = TextPaint().apply {
        textSize = spToPx(context, 13).toFloat()
        color = Color.Gray.toArgb()
    }

    private val groupTextPaint = TextPaint().apply {
        textSize = spToPx(context, 14).toFloat()
        color = Color.Black.toArgb()
        isFakeBoldText = true
    }

    private val rect = Rect()

    private val groupSpacing = dpToPx(context, 12)
    private val groupTitleVerticalSpacing = dpToPx(context, 4)
    private val valueSpacing = spToPx(context, 8)
    private val labelSpacing = spToPx(context, 4)

    private var dataMap: Map<String, List<Pair<String, String>>>? = null

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MedicineVisibilityInfoView, 0, 0).also { typedArray ->
            try {
                typedArray.getResourceId(R.styleable.MedicineVisibilityInfoView_values, 0).also {
                    if (it != 0) {
                        context.resources.openRawResource(it).bufferedReader().use { reader ->
                            reader.readText()
                        }.also { jsonStr ->
                            val json = Json { ignoreUnknownKeys = true }
                            json.decodeFromString<GranuleIdentificationInfoDto>(jsonStr).apply {
                                setData(this)
                            }
                        }

                    }
                }

            } finally {
                typedArray.recycle()
            }
        }
    }


    private fun setData(medicineVisibilityDto: GranuleIdentificationInfoDto) {

        dataMap = mutableMapOf<String, List<Pair<String, String>>>().apply {
            // 그룹 1: 의약품 정보
            context.resources.getStringArray(R.array.medicineInfo).also {
                this[context.getString(R.string.medicineInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.itemSeq,
                    it[1] to medicineVisibilityDto.itemName,
                    it[2] to medicineVisibilityDto.itemEngName
                )
            }

            // 그룹 2: 업체 정보
            context.resources.getStringArray(R.array.companyInfo).also {
                this[context.getString(R.string.companyInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.entpSeq, it[1] to medicineVisibilityDto.entpName, it[2] to medicineVisibilityDto.bizrNo
                )
            }

            // 그룹 3: 의약품 분류 정보
            context.resources.getStringArray(R.array.classificationInfo).also {
                this[context.getString(R.string.classificationInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.classNo,
                    it[1] to medicineVisibilityDto.className,
                    it[2] to medicineVisibilityDto.etcOtcName
                )
            }

            // 그룹 4: 기타 정보
            context.resources.getStringArray(R.array.miscInfo).also {
                this[context.getString(R.string.miscInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.formCodeName,
                    it[1] to medicineVisibilityDto.itemPermitDate.toString(),
                    it[2] to medicineVisibilityDto.changeDate.toString()
                )
            }

            // 그룹 5: 인쇄 및 이미지 정보
            context.resources.getStringArray(R.array.printAndImageInfo).also {
                this[context.getString(R.string.printAndImageInfoTitle)] = listOf(
                    it[0] to (medicineVisibilityDto.printFront ?: "없음"),
                    it[1] to (medicineVisibilityDto.printBack ?: "없음"),
                    it[2] to medicineVisibilityDto.markCodeFrontAnal,
                    it[3] to medicineVisibilityDto.markCodeBackAnal,
                    it[4] to medicineVisibilityDto.markCodeFrontImg,
                    it[5] to medicineVisibilityDto.markCodeBackImg
                )
            }

            // 그룹 6: 의약품 이미지 정보
            context.resources.getStringArray(R.array.medicineImageInfo).also {
                this[context.getString(R.string.medicineImageInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.chart,
                    it[1] to medicineVisibilityDto.itemImage,
                )
            }

            // 그룹 7: 의약품 형태 정보
            context.resources.getStringArray(R.array.medicineShapeInfo).also {
                this[context.getString(R.string.medicineShapeInfoTitle)] = listOf(
                    it[0] to medicineVisibilityDto.drugShape,
                    it[1] to medicineVisibilityDto.colorClass1,
                    it[2] to (medicineVisibilityDto.colorClass2 ?: "없음"),
                )
            }

            // 그룹 8: 구분선 정보
            context.resources.getStringArray(R.array.lineInfo).also {
                this[context.getString(R.string.lineInfoTitle)] = listOf(
                    it[0] to (medicineVisibilityDto.lineFront ?: "없음"),
                    it[1] to (medicineVisibilityDto.lineBack ?: "없음"),
                    it[2] to medicineVisibilityDto.lengLong,
                    it[3] to medicineVisibilityDto.lengShort,
                    it[4] to (medicineVisibilityDto.thick ?: "없음"),
                    it[5] to medicineVisibilityDto.imgRegistTs.toString(),
                )
            }

        }.toMap()


    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // data 그리기
        dataMap?.apply {
            var y = paddingTop.toFloat()

            for (group in this) {

                y += drawGroup(canvas, y, group.key, group.value)
            }
        }
    }

    /**
     * 식별 정보에 대해 연관성있는 데이터 별로 그립니다.
     */
    private fun drawGroup(canvas: Canvas, startY: Float, groupTitle: String, items: List<Pair<String, String>>): Float {
        var y = startY

        canvas.drawText(groupTitle, paddingLeft.toFloat(), y, groupTextPaint)
        y += groupTitleVerticalSpacing

        val valueX = paddingLeft + labelSpacing

        for (item in items) {
            canvas.drawText(item.first, paddingLeft.toFloat(), y, labelTextPaint)
            labelTextPaint.getTextBounds(item.first, 0, item.first.length, rect)
            canvas.drawText(item.second, (valueX + rect.width()).toFloat(), y, valueTextPaint)

            y += valueSpacing
        }

        return y + groupSpacing
    }


}