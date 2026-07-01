package com.wordscroll.home.tab.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wordscroll.data.model.PoetModel
import kotlin.math.absoluteValue

private val AvatarPalette = listOf(
    Color(0xFFB0623B), Color(0xFF5B7B7A), Color(0xFF8C6A4F),
    Color(0xFF6E5773), Color(0xFF4F6B8C), Color(0xFF7A6A53),
)

@Composable
fun PoetCard(poet: PoetModel, onClick: (poetName: String) -> Unit) {
    val avatarColor = remember(poet.name) {
        AvatarPalette[poet.name.hashCode().absoluteValue % AvatarPalette.size]
    }
    val initials = remember(poet.name) {
        poet.name.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
    }

    Column(
        modifier = Modifier
            .clickable { onClick(poet.name) }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials, color = Color.White, style = MaterialTheme.typography.titleMedium)
        }
        Text(
            text = poet.name,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
