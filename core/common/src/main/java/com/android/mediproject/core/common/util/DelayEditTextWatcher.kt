package com.android.mediproject.core.common.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

/**
 * EditText에 TextWatcher에 delay를 주고 실시간 검색 등의 기능이 구현가능하도록 하는 함수
 */
fun EditText.delayTextChangedCallback(): Flow<CharSequence?> = callbackFlow {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(text)
        }
    }
    // TextWatcher를 등록
    addTextChangedListener(listener)
    // Flow가 종료되면 TextWatcher를 제거
    awaitClose { removeTextChangedListener(listener) }
}.onStart {
    // Flow가 시작되면 EditText의 text를 emit
    emit(text)
}