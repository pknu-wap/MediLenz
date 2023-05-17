package com.android.mediproject.feature.medicine.precautions.item.precautions

import android.text.Spannable
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MedicinePrecautionsViewModel @Inject  constructor() : BaseViewModel(){

    private val _precautions = MutableStateFlow<UiState<Spannable>>(UiState.Initial)
    val precautions get() = _precautions.asStateFlow()


    fun createPrecautionsTexts(){
        viewModelScope.launch {
            val stringBuilder = WeakReference<StringBuilder>(StringBuilder())

            stringBuilder.get()?.let { builder ->
                article.contentList.forEachIndexed { index, content ->
                    builder.append(content)
                    if (index != article.contentList.size - 1) builder.append("\n")
                }

                this.contentsTextView.text = builder.toString()
                builder.clear()
            }
            */
        }
    }


    /*

}