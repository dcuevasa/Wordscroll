package com.wordscroll

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wordscroll.core.DestinationRoute.FORMATTED_POEM_READER_ROUTE
import com.wordscroll.core.DestinationRoute.HOME_SCREEN_ROUTE
import com.wordscroll.core.DestinationRoute.SAVED_ROUTE
import com.wordscroll.theme.WordscrollTheme
import com.wordscroll.component.BottomBar
import com.wordscroll.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen() {
    val navController = rememberNavController()
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntryAsState?.destination
    val context = LocalContext.current

    val isShowBottomBar = when (currentDestination?.route) {
        HOME_SCREEN_ROUTE, SAVED_ROUTE, null -> true
        else -> false
    }
    val darkMode = when (currentDestination?.route) {
        HOME_SCREEN_ROUTE, SAVED_ROUTE, FORMATTED_POEM_READER_ROUTE, null -> true
        else -> false
    }

    if (currentDestination?.route == HOME_SCREEN_ROUTE) {
        BackHandler {
            (context as? Activity)?.finish()
        }
    }

    WordscrollTheme(darkTheme = darkMode) {
        SetupSystemUi(rememberSystemUiController(), MaterialTheme.colorScheme.background)
        Scaffold(
            bottomBar = {
                if (!isShowBottomBar) {
                    return@Scaffold
                }
                BottomBar(navController, currentDestination)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun SetupSystemUi(
    systemUiController: SystemUiController,
    systemBarColor: Color
) {
    SideEffect {
        systemUiController.setSystemBarsColor(color = systemBarColor)
    }
}
