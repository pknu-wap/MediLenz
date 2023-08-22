package com.android.mediproject.core.model.local.navargs

data class RecallDisposalArgs(
    val product: String,
) : BaseNavArgs(RecallDisposalArgs::class.java.name)