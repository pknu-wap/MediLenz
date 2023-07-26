package com.android.mediproject.core.data.dur.duringr

import com.android.mediproject.core.network.datasource.dur.DurIngrDataSource
import javax.inject.Inject

class DurIngrRepositoryImpl @Inject constructor(
    durIngrDataSource: DurIngrDataSource,
) : DurIngrRepository {}
