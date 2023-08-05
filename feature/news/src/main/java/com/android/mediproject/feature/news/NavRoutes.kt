package com.android.mediproject.feature.news

sealed interface NavRoutes {
    val name: String
    val uri: String
}

sealed class MainRoutes : NavRoutes {
    sealed class News : MainRoutes() {
        override val name: String = Companion.name
        override val uri: String = Companion.uri

        companion object : MainRoutes() {
            override val name: String = "News"
            override val uri: String = "news"
        }
    }
}

sealed class RecallSaleSuspensionRoutes : MainRoutes.News() {
    override val name: String = "RecallSaleSuspension"
    override val uri: String = "${super.uri}/recall-sale-suspension"

    object RecallSaleSuspensionRoutesList : RecallSaleSuspensionRoutes() {
        override val name: String = "RecallSaleSuspensionList"
        override val uri: String = "${super.uri}/list"
    }

    object RecallSaleSuspensionRoutesDetail : RecallSaleSuspensionRoutes() {
        override val name: String = "RecallSaleSuspensionDetail"
        override val uri: String = "${super.uri}/detail"
    }
}

sealed class AdminActionRoutes : MainRoutes.News() {
    override val name: String = "AdminAction"
    override val uri: String = "${super.uri}/admin-action"

    object AdminActionRoutesList : AdminActionRoutes() {
        override val name: String = "AdminActionList"
        override val uri: String = "${super.uri}/list"
    }

    object AdminActionRoutesDetail : AdminActionRoutes() {
        override val name: String = "AdminActionDetail"
        override val uri: String = "${super.uri}/detail"
    }
}

sealed class SafetyNotificationRoutes : MainRoutes.News() {
    override val name: String = "SafetyNotification"
    override val uri: String = "${super.uri}/safety-notification"

    object SafetyNotificationRoutesList : SafetyNotificationRoutes() {
        override val name: String = "SafetyNotificationList"
        override val uri: String = "${super.uri}/list"
    }

    object SafetyNotificationRoutesDetail : SafetyNotificationRoutes() {
        override val name: String = "SafetyNotificationDetail"
        override val uri: String = "${super.uri}/detail"
    }
}
