package com.android.mediproject.feature.news

import androidx.navigation.NavType

sealed class NavRoutes {
    abstract val name: String
    open val arguments: List<Pair<String, NavType<*>>> = listOf()
    val uri: String
        get() {
            return if (arguments.isEmpty()) name else "$name/${arguments.joinToString("/") { "{${it.first}}" }}"
        }

    fun uriWithArguments(args: List<Pair<String, Any>>): String {
        return if (args.isEmpty()) uri else args.run {
            var finalUri = uri
            forEach { finalUri = finalUri.replace("{${it.first}}", it.second.toString()) }
            finalUri
        }
    }
}

sealed class MainRoutes : NavRoutes() {
    sealed class News : MainRoutes() {
        override val name: String = Companion.name

        companion object : MainRoutes() {
            override val name: String = "News"
        }
    }
}

sealed class RecallSaleSuspensionRoutes : MainRoutes.News() {
    override val name: String = "RecallSaleSuspension"
    override val arguments: List<Pair<String, NavType<*>>> = listOf()

    object RecallSaleSuspensionRoutesList : RecallSaleSuspensionRoutes() {
        override val name: String = "RecallSaleSuspensionList"
        override val arguments: List<Pair<String, NavType<*>>> = listOf()

    }

    object RecallSaleSuspensionRoutesDetail : RecallSaleSuspensionRoutes() {
        override val name: String = "RecallSaleSuspensionDetail"
        override val arguments: List<Pair<String, NavType<*>>> = listOf("product" to NavType.StringType)

    }
}

sealed class AdminActionRoutes : MainRoutes.News() {
    override val name: String = "AdminAction"
    override val arguments: List<Pair<String, NavType<*>>> = listOf()

    object AdminActionRoutesList : AdminActionRoutes() {
        override val name: String = "AdminActionList"
        override val arguments: List<Pair<String, NavType<*>>> = listOf()

    }

    object AdminActionRoutesDetail : AdminActionRoutes() {
        override val name: String = "AdminActionDetail"
        override val arguments: List<Pair<String, NavType<*>>> = listOf()

    }
}

sealed class SafetyNotificationRoutes : MainRoutes.News() {
    override val name: String = "SafetyNotification"
    override val arguments: List<Pair<String, NavType<*>>> = listOf()

    object SafetyNotificationRoutesList : SafetyNotificationRoutes() {
        override val name: String = "SafetyNotificationList"
        override val arguments: List<Pair<String, NavType<*>>> = listOf()
    }

    object SafetyNotificationRoutesDetail : SafetyNotificationRoutes() {
        override val name: String = "SafetyNotificationDetail"
        override val arguments: List<Pair<String, NavType<*>>> = listOf()
    }
}
