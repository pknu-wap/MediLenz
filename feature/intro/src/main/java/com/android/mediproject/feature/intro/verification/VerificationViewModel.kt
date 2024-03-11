package com.android.mediproject.feature.intro.verification

import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val signRepository: SignRepository,
) : BaseViewModel() {
    
}
