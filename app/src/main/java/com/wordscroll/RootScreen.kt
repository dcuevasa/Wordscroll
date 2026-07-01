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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wordscroll.core.DestinationRoute.HOME_SCREEN_ROUTE
import com.wordscroll.core.DestinationRoute.SAVED_ROUTE
import com.wordscroll.core.DestinationRoute.SETTINGS_ROUTE
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.theme.LocalizedApp
import com.wordscroll.theme.WordscrollTheme
import com.wordscroll.component.BottomBar
import com.wordscroll.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(appViewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntryAsState?.destination
    val context = LocalContext.current

    val theme by appViewModel.theme.collectAsState()
    val language by appViewModel.language.collectAsState()

    val isShowBottomBar = when (currentDestination?.route) {
        HOME_SCREEN_ROUTE, SAVED_ROUTE, SETTINGS_ROUTE, null -> true
        else -> false
    }

    if (currentDestination?.route == HOME_SCREEN_ROUTE) {
        BackHandler {
            (context as? Activity)?.finish()
        }
    }

    LocalizedApp(languageTag = language.tag) {
        WordscrollTheme(config = theme) {
            SetupSystemUi(rememberSystemUiController(), theme)
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
}

@Composable
fun SetupSystemUi(
    systemUiController: SystemUiController,
    theme: ThemeConfig
) {
    val barColor = MaterialTheme.colorScheme.background
    SideEffect {
        systemUiController.setSystemBarsColor(color = barColor, darkIcons = theme.darkSystemIcons)
    }
}
