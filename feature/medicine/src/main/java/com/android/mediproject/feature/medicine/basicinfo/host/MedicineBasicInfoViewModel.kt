package com.android.mediproject.feature.medicine.basicinfo.host

import androidx.lifecycle.SavedStateHandle
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.medicine.main.BasicInfoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MedicineBasicInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val basicInfoType: StateFlow<BasicInfoType> =
        savedStateHandle.getStateFlow<BasicInfoType>("basicInfoType", BasicInfoType.EFFICACY_EFFECT)
}