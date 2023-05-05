package com.android.mediproject.feature.medicine

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.model.medicine.MedicineVisibilityDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor() : BaseViewModel() {

    private val _visibilityInfo = MutableEventFlow<MedicineVisibilityDto>(replay = 1)
    val visibilityInfo = _visibilityInfo.asEventFlow()

    fun loadVisibilityInfo() {
        viewModelScope.launch {
            _visibilityInfo.emit(MedicineVisibilityDto())
        }
    }

}