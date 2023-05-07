package com.android.mediproject.feature.visibility

import androidx.lifecycle.SavedStateHandle
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisibilityInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    // TODO: Implement the ViewModel
}