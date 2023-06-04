package com.android.mediproject.feature.interestedmedicine

import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InterstedMedicineViewModel @Inject constructor() : BaseViewModel() {

    //dummy for test
    val dummy = listOf(
        InterestedMedicineDto(1,"타이레놀", System.currentTimeMillis().toString()),
        InterestedMedicineDto(2,"가나다라", System.currentTimeMillis().toString()),
        InterestedMedicineDto(2,"ABCD", System.currentTimeMillis().toString()),
        InterestedMedicineDto(2,"EFGH", System.currentTimeMillis().toString()),
        InterestedMedicineDto(3,"에이비시", System.currentTimeMillis().toString())
    ).sortedBy { it.createdAt }

    private val _interstedMedicineList = MutableStateFlow<List<InterestedMedicineDto>>(dummy)
    val interstedMedicineList get() = _interstedMedicineList
}