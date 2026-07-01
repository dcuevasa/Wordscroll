package com.wordscroll.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wordscroll.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    @DrawableRes navIcon: Int? = R.drawable.ic_arrow_back,
    title: String? = null,
    backgroundColor: Color = Color.Transparent,
    actions: @Composable RowScope.() -> Unit = {},
    onClickNavIcon: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            navIcon?.let {
                Icon(painter = painterResource(id = navIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onClickNavIcon() })
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundColor),
        actions = {
            actions()
        },
    )
}