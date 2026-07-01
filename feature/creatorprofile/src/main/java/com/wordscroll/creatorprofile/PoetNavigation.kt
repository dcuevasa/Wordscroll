package com.wordscroll.creatorprofile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wordscroll.core.DestinationRoute.FORMATTED_POEM_READER_ROUTE
import com.wordscroll.core.DestinationRoute.FORMATTED_POET_PROFILE_ROUTE
import com.wordscroll.core.DestinationRoute.PassedKey.POEM_INDEX
import com.wordscroll.core.DestinationRoute.PassedKey.POET_NAME
import com.wordscroll.creatorprofile.screen.poemreader.PoemReaderScreen
import com.wordscroll.creatorprofile.screen.poetprofile.PoetProfileScreen

fun NavGraphBuilder.poetProfileNavGraph(navController: NavController) {
    composable(
        route = FORMATTED_POET_PROFILE_ROUTE,
        arguments = listOf(navArgument(POET_NAME) { type = NavType.StringType })
    ) {
        PoetProfileScreen(
            onClickNavIcon = { navController.navigateUp() },
            navController = navController
        )
    }

    composable(
        route = FORMATTED_POEM_READER_ROUTE,
        arguments = listOf(
            navArgument(POET_NAME) { type = NavType.StringType },
            navArgument(POEM_INDEX) { type = NavType.IntType }
        )
    ) {
        PoemReaderScreen(
            onClickNavIcon = { navController.navigateUp() }
        )
    }
}
