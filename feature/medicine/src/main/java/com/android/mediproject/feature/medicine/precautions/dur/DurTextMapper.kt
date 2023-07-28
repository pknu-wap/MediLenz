package com.android.mediproject.feature.medicine.precautions.dur

import android.content.Context
import android.text.Html
import android.text.Spanned
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.feature.medicine.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class DurTextMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val _durTitleAndDescriptions = MutableStateFlow<Map<DurType, DurTitleAndDescription>?>(null)
    val durTitleAndDescriptions get() = _durTitleAndDescriptions.asStateFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val durTitleAndDescriptionMap = mutableMapOf<DurType, DurTitleAndDescription>()

            context.run {
                durTitleAndDescriptionMap[DurType.DOSING_CAUTION] = DurTitleAndDescription(
                    createTitle(getString(R.string.dosingCaution)),
                    createDefinition(
                        getString(R.string.dosingCautionDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.EX_RELEASE_TABLET_SPLIT_ATTENTION] = DurTitleAndDescription(
                    createTitle(getString(R.string.exReleaseTabletSplitAttention)),
                    createDefinition(
                        getString(R.string.exReleaseTabletSplitAttentionDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.PREGNANT_WOMAN_TABOO] = DurTitleAndDescription(
                    createTitle(getString(R.string.pregnancyTaboo)),
                    createDefinition(
                        getString(R.string.pregnancyTabooDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.COMBINATION_TABOO] = DurTitleAndDescription(
                    createTitle(getString(R.string.combinationTaboo)),
                    createDefinition(
                        getString(R.string.combinationTabooDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.SENIOR_CAUTION] = DurTitleAndDescription(
                    createTitle(getString(R.string.seniorCaution)),
                    createDefinition(
                        getString(R.string.seniorCautionDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.CAPACITY_ATTENTION] = DurTitleAndDescription(
                    createTitle(getString(R.string.capacityAttention)),
                    createDefinition(
                        getString(R.string.capacityAttentionDescription),
                    ),
                )

                durTitleAndDescriptionMap[DurType.SPECIALTY_AGE_GROUP_TABOO] = DurTitleAndDescription(
                    createTitle(getString(R.string.specialtyAgeGroupTaboo)),
                    createDefinition(
                        getString(R.string.specialtyAgeGroupTaboo),
                    ),
                )

                durTitleAndDescriptionMap[DurType.EFFICACY_GROUP_DUPLICATION] = DurTitleAndDescription(
                    createTitle(getString(R.string.efficacyGroupDuplication)),
                    createDefinition(
                        getString(R.string.efficacyGroupDuplicationDescription),
                    ),
                )
            }

            _durTitleAndDescriptions.value = durTitleAndDescriptionMap
        }
    }

    private fun createTitle(value: String): Spanned = Html.fromHtml("<font size=4><b>• $value</b></font>", Html.FROM_HTML_MODE_COMPACT)

    private fun createDefinition(value: String): Spanned = Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT)

    data class DurTitleAndDescription(
        val title: Spanned,
        val description: Spanned,
    )
}
