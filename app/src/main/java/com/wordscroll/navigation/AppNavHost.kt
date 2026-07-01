package com.wordscroll.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.wordscroll.core.DestinationRoute.HOME_SCREEN_ROUTE
import com.wordscroll.creatorprofile.poetProfileNavGraph
import com.wordscroll.home.homeNavGraph
import com.wordscroll.home.saved.savedNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_SCREEN_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeNavGraph(navController)
        savedNavGraph(navController)
        poetProfileNavGraph(navController)
    }
}
