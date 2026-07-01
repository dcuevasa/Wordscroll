package com.wordscroll.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.wordscroll.navigation.BottomBarDestination

@Composable
fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    NavigationBar(
        modifier = Modifier
            .height(52.dp)
            .shadow(elevation = 16.dp)
            .padding(top = 2.dp)
    ) {
        BottomBarDestination.values().asList().forEach {
            BottomItem(it, navController, currentDestination)
        }
    }
}

@Composable
fun RowScope.BottomItem(
    screen: BottomBarDestination,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    val isCurrentBottomItemSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } ?: false

    val icon = if (isCurrentBottomItemSelected) screen.filledIcon else screen.unFilledIcon

    NavigationBarItem(
        modifier = Modifier.offset(y = -BottomBarItemVerticalOffset),
        label = {
            Text(
                modifier = Modifier.offset(y = BottomBarItemVerticalOffset.times(1.85f)),
                text = stringResource(id = screen.title),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isCurrentBottomItemSelected) 1f else 0.7f)
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 9.dp)
                    .size(22.dp),
                tint = if (isCurrentBottomItemSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.surface,
            selectedIconColor = MaterialTheme.colorScheme.secondary,
            selectedTextColor = MaterialTheme.colorScheme.secondary
        ),
        selected = isCurrentBottomItemSelected,
        onClick = {
            screen.route.let {
                navController.navigate(it) {
                    launchSingleTop = true
                }
            }
        }
    )
}

private val BottomBarItemVerticalOffset = 10.dp
