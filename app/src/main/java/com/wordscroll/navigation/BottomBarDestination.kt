package com.wordscroll.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wordscroll.core.DestinationRoute.HOME_SCREEN_ROUTE
import com.wordscroll.core.DestinationRoute.SAVED_ROUTE
import com.wordscroll.core.DestinationRoute.SETTINGS_ROUTE
import com.wordscroll.theme.R

enum class BottomBarDestination(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val unFilledIcon: Int,
    @DrawableRes val filledIcon: Int
) {
    HOME(
        route = HOME_SCREEN_ROUTE,
        title = R.string.home,
        unFilledIcon = R.drawable.ic_home,
        filledIcon = R.drawable.ic_home_fill
    ),

    SAVED(
        route = SAVED_ROUTE,
        title = R.string.saved,
        unFilledIcon = R.drawable.ic_bookmark,
        filledIcon = R.drawable.ic_bookmark
    ),

    SETTINGS(
        route = SETTINGS_ROUTE,
        title = R.string.settings,
        unFilledIcon = R.drawable.ic_settings,
        filledIcon = R.drawable.ic_settings
    ),
}
