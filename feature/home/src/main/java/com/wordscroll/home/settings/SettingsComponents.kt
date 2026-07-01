package com.wordscroll.home.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.contrastTextColor
import com.wordscroll.core.settings.isLightColor
import com.wordscroll.theme.R
import com.wordscroll.theme.rememberBackgroundPainter
import com.wordscroll.theme.textDecoration
import com.wordscroll.theme.toFontFamily

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else Color.Transparent
            )
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ColorSwatchRow(colors: List<Long>, selected: Long, onSelect: (Long) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(colors) { argb ->
            val isSelected = argb == selected
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(argb))
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable { onSelect(argb) }
            )
        }
    }
}

@Composable
fun ThemePreview(config: ThemeConfig, modifier: Modifier = Modifier) {
    val bgPainter = config.rememberBackgroundPainter()
    val textStyle = TextStyle(
        fontFamily = config.font.toFontFamily(),
        fontStyle = if (config.italic) FontStyle.Italic else FontStyle.Normal,
        textDecoration = config.textDecoration(),
        color = Color(config.textColor)
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(config.backgroundColor))
    ) {
        if (bgPainter != null) {
            Image(
                painter = bgPainter,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(modifier = Modifier.padding(18.dp)) {
            // This mirrors how a poem will actually render, so it scales with
            // poemFontScale — not the app/menu font scale.
            Text(
                text = stringResource(id = R.string.preview_title),
                style = textStyle.copy(fontSize = 20.sp * config.poemFontScale, fontStyle = FontStyle.Italic),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(id = R.string.preview_sample),
                style = textStyle.copy(
                    fontSize = 16.sp * config.poemFontScale,
                    lineHeight = 24.sp * config.poemFontScale
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun ThemeCard(
    displayName: String,
    config: ThemeConfig,
    selected: Boolean,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.size(width = 84.dp, height = 150.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(width = 84.dp, height = 116.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(config.backgroundColor))
                    .border(
                        width = if (selected) 3.dp else 1.dp,
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onClick() }
            ) {
                config.rememberBackgroundPainter()?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = "Aa",
                    style = TextStyle(
                        fontFamily = config.font.toFontFamily(),
                        fontStyle = if (config.italic) FontStyle.Italic else FontStyle.Normal,
                        textDecoration = config.textDecoration(),
                        color = Color(config.textColor),
                        fontSize = 30.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(config.accentColor))
                )
            }
            if (onDelete != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(18.dp)
                        .clickable { onDelete() }
                )
            }
        }
        Text(
            text = displayName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        )
    }
}
