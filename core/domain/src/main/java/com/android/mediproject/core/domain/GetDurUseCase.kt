package com.android.mediproject.core.domain

import com.android.mediproject.core.data.dur.durproduct.DurProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDurUseCase @Inject constructor(
    private val durProductRepository: DurProductRepository,
    private val durIngrRepository: DurProductRepository,
) {

}
