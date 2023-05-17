package com.android.mediproject.feature.medicine.precautions.host

import android.text.Spanned
import androidx.core.text.toSpannable
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MedicinePrecautionsViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _precautions = MutableStateFlow<List<Spanned>>(emptyList())
    val precautions get() = _precautions.asStateFlow()


    /**
     * 약의 주의사항을 보여주는 텍스트를 만들어주는 함수
     */
    fun createPrecautionsTexts(xmlParsedResult: XMLParsedResult) {
        viewModelScope.launch(ioDispatcher) {
            val stringBuilder = WeakReference(StringBuilder())

            stringBuilder.get()?.also { builder ->
                val list = mutableListOf<Spanned>()

                xmlParsedResult.articleList.forEach { article ->
                    builder.append("<ul><b>${article.title}<b><br>")
                    article.contentList.forEach { content ->
                        builder.append("<li>${content}</li>")
                    }
                    builder.append("<br></ul>")
                }
                list.add(builder.toString().toSpannable())
                builder.clear()

                _precautions.value = list.toList()
            }
            stringBuilder.get()?.clear()
        }
    }


}