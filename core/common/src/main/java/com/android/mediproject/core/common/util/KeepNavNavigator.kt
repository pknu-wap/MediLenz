package com.android.mediproject.core.common.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider

@Navigator.Name("keep_navigation")
class KeepNavNavigator(
    private val navigatorProvider: NavigatorProvider,
) : Navigator<NavGraph>() {

    private val backstackMap = mutableMapOf<Int, NavBackStackEntry>()

    override fun createDestination(): NavGraph {
        return NavGraph(this)
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) {
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) {
        val destination = entry.destination as NavGraph
        val args = entry.arguments
        val startId = destination.startDestinationId
        val startRoute = destination.startDestinationRoute
        check(startId != 0 || startRoute != null) {
            ("no start destination defined via app:startDestination for ${destination.displayName}")
        }
        val startDestination = if (startRoute != null) {
            destination.findNode(startRoute, false)
        } else {
            destination.findNode(startId, false)
        }
        requireNotNull(startDestination) {
            val dest = destination.startDestDisplayName
            throw IllegalArgumentException(
                "navigation destination $dest is not a direct child of this NavGraph",
            )
        }
        val navigator = navigatorProvider.getNavigator<Navigator<NavDestination>>(
            startDestination.navigatorName,
        )
        val startDestinationEntry = state.createBackStackEntry(
            startDestination, startDestination.addInDefaultArgs(args),
        )
        
        navigator.navigate(listOf(startDestinationEntry), navOptions, navigatorExtras)
    }
}
