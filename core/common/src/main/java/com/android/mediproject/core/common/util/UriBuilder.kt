package com.android.mediproject.core.common.util

import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavType
import com.android.mediproject.core.model.local.navargs.BaseNavArgs

/**
 * Uri Builder
 *
 * Uri를 생성하는 함수입니다.
 *
 * @param parameter Uri에 들어갈 파라미터
 * @return Uri
 */
private fun toDeepUrl(deepLinkUrl: String, parameter: Map<String, String>): Uri = StringBuilder(deepLinkUrl).let { uri ->
    parameter.takeIf {
        it.isNotEmpty()
    }?.also { map ->
        uri.append("?")
        map.onEachIndexed { index, entry ->
            uri.append("${entry.key}=${entry.value}")
            if (index != map.size - 1) uri.append("&")
        }
    }
    uri.toString().toUri()
}

/**
 * NavController를 확장하는 함수입니다.
 *
 * DeepLink를 통해 Navigation을 수행합니다.
 *
 * @param deepLinkUrl DeepLink Url
 * @param parameter DeepLink에 들어갈 파라미터
 */
fun NavController.navigateByDeepLink(deepLinkUrl: String, parameter: BaseNavArgs) {
    val parameterMap = parameter.toMap()
    toDeepUrl(deepLinkUrl, parameterMap).also { finalUri ->
        graph.matchDeepLink(NavDeepLinkRequest(finalUri, null, null))?.also { deepLinkMatch ->
            parameterMap.takeIf {
                it.isNotEmpty()
            }?.forEach { (key, value) ->
                deepLinkMatch.destination.addArgument(
                    key, NavArgument.Builder().setType(NavType.StringType).setIsNullable(false).setDefaultValue(value).build()
                )
            }
        }
    }

    this.navigate(deepLinkUrl.toUri())
}