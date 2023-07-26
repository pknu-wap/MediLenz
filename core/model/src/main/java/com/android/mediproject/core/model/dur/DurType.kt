package com.android.mediproject.core.model.dur

enum class DurType(val type: String) {
    CAPACITY_ATTENTION("용량주의"), DOSING_CAUTION("투여기간주의"), EFFICACY_GROUP_DUPLICATION("효능군중복"), EX_RELEASE_TABLET_SPLIT_ATTENTION("서방정분할주의"),
    COMBINATION_TABOO("병용금기"), SPECIALTY_AGE_GROUP_TABOO("특정연령대금기"), SENIOR_CAUTION("노인주의"), PREGNANT_WOMAN_TABOO("임부금기")
}
