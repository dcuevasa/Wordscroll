package com.wordscroll.creatorprofile.screen.poetprofile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wordscroll.core.extension.Space
import com.wordscroll.data.model.PoemModel
import com.wordscroll.theme.R
import com.wordscroll.theme.SeparatorColor
import com.wordscroll.theme.SubTextColor

@Composable
fun PoemListItem(
    poem: PoemModel,
    isBookmarked: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = poem.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isBookmarked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        val preview = poem.lines.firstOrNull { it.isNotBlank() }.orEmpty()
        if (preview.isNotEmpty()) {
            4.dp.Space()
            Text(
                text = preview,
                style = MaterialTheme.typography.bodySmall,
                color = SubTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        12.dp.Space()
        Divider(thickness = 0.5.dp, color = SeparatorColor)
    }
}
