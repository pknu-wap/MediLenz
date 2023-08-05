package com.android.mediproject.core.common.util

import android.net.Uri
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgsLazy
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
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
private fun toQueryUri(deepLinkUrl: String, parameter: Map<String, Any?>): Uri = StringBuilder(deepLinkUrl).let { uri ->
    parameter.takeIf {
        it.isNotEmpty()
    }?.also { map ->
        uri.append("?")
        map.onEachIndexed { index, entry ->
            if (entry.value != null) {
                uri.append("${entry.key}=${entry.value}")
                if (index != map.size - 1) uri.append("&")
            }
        }
    }
    uri.toString().toUri()
}

/**
 * Uri Builder
 *
 * 쿼리가 key만 포함된 Uri를 생성하는 함수입니다.
 *
 * @param parameter Uri에 들어갈 파라미터
 * @return Uri
 */
private fun toBaseUri(deepLinkUrl: String, parameter: Map<String, Any?>): Uri = StringBuilder(deepLinkUrl).let { uri ->
    parameter.takeIf {
        it.isNotEmpty()
    }?.also { map ->
        uri.append("?")
        map.onEachIndexed { index, entry ->
            if (entry.value != null) {
                uri.append("${entry.key}={${entry.key}}")
                if (index != map.size - 1) uri.append('&')
            }
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
fun NavController.navigateByDeepLink(
    deepLinkUrl: String, parameter: BaseNavArgs, navOptions: NavOptions? = null
) {
    val parameterMap = parameter.toMap()
    toQueryUri(deepLinkUrl, parameterMap).also { finalUri ->
        graph.matchDeepLink(NavDeepLinkRequest(finalUri, null, null))?.also { deepLinkMatch ->
            parameterMap.takeIf {
                it.isNotEmpty()
            }?.forEach { (key, value) ->

                val navType: NavType<out Any?> = when (value) {
                    is String -> NavType.StringType
                    is Int -> NavType.IntType
                    is Long -> NavType.LongType
                    is Float -> NavType.FloatType
                    is Boolean -> NavType.BoolType
                    else -> NavType.ReferenceType
                }

                deepLinkMatch.destination.addArgument(key,
                    NavArgument.Builder().setType(navType).setIsNullable(true).setDefaultValue(value).build())
            }
        }
    }

    this.navigate(deepLinkUrl.toUri(), navOptions)
}


fun NavController.deepNavigate(
    deepLinkUrl: String, parameter: BaseNavArgs, navOptions: NavOptions? = null
) {
    val parameterMap = parameter.toMap()

    // "medilenz://main?name={name}&age={age}"
    val baseUri = toBaseUri(deepLinkUrl, parameterMap)
    // "medilenz://main?name=NAME&age=AGE"
    val finalUri = toQueryUri(deepLinkUrl, parameterMap)

    graph.matchDeepLink(NavDeepLinkRequest(baseUri, null, null))?.apply {
        // finalUri를 도착지 그래프에 새로운 딥링크로 추가
        if (!destination.arguments.contains(NAV_KEY)) {
            destination.addArgument(NAV_KEY, NAV_ARG)
            destination.addDeepLink(NavDeepLink.Builder().setUriPattern(baseUri.toString()).build())
        }
    }

    navigate(finalUri, navOptions)
}

inline fun <reified N : BaseNavArgs> NavDestination.setArguments(navArgs: N) {
    navArgs.toMap().forEach { (key, value) ->
        if (value == null) return@forEach
        val navType: NavType<out Any?> = when (value) {
            is String -> NavType.StringType
            is Int -> NavType.IntType
            is Long -> NavType.LongType
            is Float -> NavType.FloatType
            is Boolean -> NavType.BoolType
            else -> NavType.ReferenceType
        }

        addArgument(key, NavArgument.Builder().setType(navType).setIsNullable(false).setDefaultValue(value).build())
    }
}


@MainThread
inline fun <reified Args : BaseNavArgs> Fragment.navArgs(): NavArgsLazy<out Args> = NavArgsLazy(Args::class) {
    val bundle = arguments ?: Bundle()
    bundle.apply {
        putString("className", Args::class.java.name)
        remove(NAV_KEY)
    }
}

const val NAV_KEY = "__Deep_Args_"
private val NAV_ARG = NavArgument.Builder().setType(NavType.BoolType).setDefaultValue(true).build()