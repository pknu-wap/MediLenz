package com.android.mediproject.feature.interestedmedicine

import com.android.mediproject.core.model.medicine.InterestedMedicine.MedicineInterestedDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class InterstedMedicineViewModel @Inject constructor() : BaseViewModel() {

    //dummy for test
    val dummy = listOf(
        MedicineInterestedDto(1,"타이레놀", System.currentTimeMillis().toString()),
        MedicineInterestedDto(2,"가나다라", System.currentTimeMillis().toString()),
        MedicineInterestedDto(2,"ABCD", System.currentTimeMillis().toString()),
        MedicineInterestedDto(2,"EFGH", System.currentTimeMillis().toString()),
        MedicineInterestedDto(3,"에이비시", System.currentTimeMillis().toString())
    ).sortedBy { it.createdAt }

    private val _interstedMedicineList = MutableStateFlow<List<MedicineInterestedDto>>(dummy)
    val interstedMedicineList get() = _interstedMedicineList
}