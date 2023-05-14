package com.android.mediproject.core.common.util

import android.net.Uri
import androidx.core.net.toUri

/**
 * Uri Builder
 *
 * Uri를 생성하는 함수입니다.
 *
 * @param parameter Uri에 들어갈 파라미터
 * @return Uri
 */
infix fun String.toUri(parameter: Map<String, String>): Uri = StringBuilder(this).let { uri ->
    parameter.filter { it.value.isNotEmpty() }.also { map ->
        uri.append("?")
        map.onEachIndexed { index, entry ->
            uri.append("${entry.key}=${entry.value}")
            if (index != map.size - 1) uri.append("&")
        }
    }
    uri.toString().toUri()
}