package com.android.mediproject.feature.medicine.precautions.host

import android.text.Html
import android.text.Spanned
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MedicinePrecautionsViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _precautions = MutableStateFlow<List<Spanned>>(emptyList())
    val precautions get() = _precautions.asStateFlow()


    /**
     * 약의 주의사항을 보여주는 텍스트를 만들어주는 함수
     */
    fun createPrecautionsTexts(xmlParsedResult: XMLParsedResult) {
        viewModelScope.launch(defaultDispatcher) {
            val stringBuilder = WeakReference(StringBuilder())

            stringBuilder.get()?.also { builder ->
                val list = mutableListOf<Spanned>()

                xmlParsedResult.articleList.forEach { article ->
                    if (article.contentList.isNotEmpty()) builder.append("<b>${article.title}</b><br>")
                    else builder.append("<b>${article.title}</b><br><br>")

                    builder.append(article.contentList)

                    if (article.contentList.isNotEmpty()) builder.append("<br><br>")
                }
                list.add(Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT))
                builder.clear()

                _precautions.update {
                    list.toList()
                }
            }
            stringBuilder.clear()
        }
    }


}