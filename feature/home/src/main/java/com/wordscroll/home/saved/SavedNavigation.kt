package com.wordscroll.home.saved

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wordscroll.core.DestinationRoute.SAVED_ROUTE

fun NavGraphBuilder.savedNavGraph(navController: NavController) {
    composable(route = SAVED_ROUTE) {
        SavedScreen(navController)
    }
}
