package com.wordscroll.home.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wordscroll.core.DestinationRoute.SETTINGS_ROUTE

fun NavGraphBuilder.settingsNavGraph() {
    composable(route = SETTINGS_ROUTE) {
        SettingsScreen()
    }
}
