package com.wordscroll.home.settings

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wordscroll.core.settings.AppFont
import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.GithubPoemSource
import com.wordscroll.core.settings.ImageCorner
import com.wordscroll.core.settings.PoemBackground
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.ThemePresets
import com.wordscroll.core.settings.contrastTextColor
import com.wordscroll.core.settings.isLightColor
import com.wordscroll.theme.R
import com.wordscroll.theme.WordscrollTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private val BackgroundSwatches = listOf(
    0xFF14130F, 0xFF000000, 0xFF0D1117, 0xFF0B3D91, 0xFF1B2A1B,
    0xFFFBF8F3, 0xFFF4ECD8, 0xFFFAF3E0, 0xFFD9C3A5, 0xFFFFF3B0
)
private val TextSwatches = listOf(
    0xFFEDEAE3, 0xFFFFFFFF, 0xFF7EE787, 0xFF33FF66, 0xFFE6F0FF,
    0xFF1C1B1A, 0xFF5B4636, 0xFF4A3520, 0xFF2B2D42, 0xFFEF476F
)
private val AccentSwatches = listOf(
    0xFFB0623B, 0xFFA0552B, 0xFF7B5230, 0xFF58A6FF, 0xFF00E5FF,
    0xFFEF476F, 0xFF2A9D8F, 0xFFF4A261, 0xFF9FC3FF, 0xFFEDEAE3
)
private val PickableBackgrounds = PoemBackground.values().filterNot { it == PoemBackground.CUSTOM_IMAGE }

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val state = viewState ?: return
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val current = remember(state.selectedThemeId, state.themes) {
        state.themes.firstOrNull { it.id == state.selectedThemeId } ?: ThemePresets.default
    }
    var draftState by remember { mutableStateOf<ThemeConfig?>(null) }
    val draft = draftState ?: current
    var customName by remember { mutableStateOf("") }

    val defaultCustomName = stringResource(id = R.string.custom_theme_default)

    val backgroundImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val path = copyImageToInternalStorage(context, uri, prefix = "bg")
            if (path != null) {
                draftState = draft.copy(background = PoemBackground.CUSTOM_IMAGE, customBackgroundPath = path)
            }
        }
    }

    // Picking a companion image always makes it visible immediately — if no
    // corner was chosen yet, default to top-end (clear of everything else).
    val companionImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val path = copyImageToInternalStorage(context, uri, prefix = "companion")
            if (path != null) {
                draftState = draft.copy(
                    companionImagePath = path,
                    companionImageCorner = if (draft.companionImageCorner == ImageCorner.NONE) {
                        ImageCorner.TOP_END
                    } else draft.companionImageCorner
                )
            }
        }
    }

    // The whole screen previews the in-progress draft live — including its
    // own background/text colors — so contrast problems are visible (and
    // fixable) the moment you pick a color, not just after saving.
    WordscrollTheme(config = draft) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(top = 44.dp, bottom = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // ---- Language ----
            SectionTitle(text = stringResource(id = R.string.language))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(AppLanguage.values().toList()) { lang ->
                    SelectableChip(
                        text = languageLabel(lang),
                        selected = state.language == lang,
                        onClick = { viewModel.setLanguage(lang) }
                    )
                }
            }

            // ---- Poem sources ----
            SectionTitle(text = stringResource(id = R.string.poem_sources))
            PoemSourcesSection(
                sources = state.poemSources,
                onAddSource = { owner, repo, branch -> viewModel.addGithubSource(owner, repo, branch) },
                onRemoveSource = { viewModel.removeGithubSource(it) }
            )

            // ---- Theme presets & custom ----
            SectionTitle(text = stringResource(id = R.string.theme))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.themes, key = { it.id }) { theme ->
                    ThemeCard(
                        displayName = themeDisplayName(theme),
                        config = theme,
                        selected = theme.id == state.selectedThemeId,
                        onClick = {
                            draftState = theme
                            viewModel.selectTheme(theme.id)
                        },
                        onDelete = if (theme.isCustom) {
                            { viewModel.deleteTheme(theme.id) }
                        } else null
                    )
                }
            }

            // ---- Customize ----
            SectionTitle(text = stringResource(id = R.string.customize))
            ThemePreview(
                config = draft,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(140.dp)
            )

            Label(text = stringResource(id = R.string.font))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(AppFont.values().toList()) { font ->
                    SelectableChip(
                        text = fontLabel(font),
                        selected = draft.font == font,
                        onClick = { draftState = draft.copy(font = font) }
                    )
                }
            }

            Label(text = stringResource(id = R.string.background))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(PickableBackgrounds) { bg ->
                    SelectableChip(
                        text = backgroundLabel(bg),
                        selected = draft.background == bg,
                        onClick = { draftState = draft.copy(background = bg) }
                    )
                }
                item {
                    val isCustomSelected = draft.background == PoemBackground.CUSTOM_IMAGE
                    SelectableChip(
                        text = stringResource(
                            id = if (isCustomSelected) R.string.change_image else R.string.upload_image
                        ),
                        selected = isCustomSelected,
                        onClick = { backgroundImagePicker.launch("image/*") }
                    )
                }
            }

            // A small "sticker" image in a corner of the poem screen, on top
            // of (and separate from) the full-bleed background above.
            Label(text = stringResource(id = R.string.companion_image))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ImageCorner.values().toList()) { corner ->
                    SelectableChip(
                        text = cornerLabel(corner),
                        selected = draft.companionImageCorner == corner,
                        onClick = { draftState = draft.copy(companionImageCorner = corner) }
                    )
                }
                item {
                    SelectableChip(
                        text = stringResource(
                            id = if (draft.companionImagePath != null) R.string.change_image else R.string.upload_image
                        ),
                        selected = false,
                        onClick = { companionImagePicker.launch("image/*") }
                    )
                }
            }

            // Background color drives the poem canvas AND (unless the toggle
            // below is on) the app chrome too — so picking one always assigns
            // a readable default text color for both, which you can still
            // override yourself right below.
            Label(text = stringResource(id = R.string.background_color))
            ColorSwatchRow(
                colors = BackgroundSwatches,
                selected = draft.backgroundColor,
                onSelect = { newBg ->
                    draftState = draft.copy(
                        backgroundColor = newBg,
                        darkSystemIcons = isLightColor(newBg),
                        textColor = contrastTextColor(newBg),
                        uiTextColor = if (draft.useSeparateUiTextColor) contrastTextColor(newBg) else draft.uiTextColor
                    )
                }
            )

            Label(text = stringResource(id = R.string.accent_color))
            ColorSwatchRow(
                colors = AccentSwatches,
                selected = draft.accentColor,
                onSelect = { newAccent ->
                    draftState = draft.copy(accentColor = newAccent, onAccentColor = contrastTextColor(newAccent))
                }
            )

            Label(text = stringResource(id = R.string.poem_text_color))
            ColorSwatchRow(
                colors = TextSwatches,
                selected = draft.textColor,
                onSelect = { draftState = draft.copy(textColor = it) }
            )

            ToggleRow(
                label = stringResource(id = R.string.use_separate_ui_text_color),
                checked = draft.useSeparateUiTextColor,
                onCheckedChange = { enabled ->
                    draftState = draft.copy(
                        useSeparateUiTextColor = enabled,
                        uiTextColor = if (enabled) contrastTextColor(draft.backgroundColor) else draft.uiTextColor
                    )
                }
            )
            if (draft.useSeparateUiTextColor) {
                Label(text = stringResource(id = R.string.app_text_color))
                ColorSwatchRow(
                    colors = TextSwatches,
                    selected = draft.uiTextColor,
                    onSelect = { draftState = draft.copy(uiTextColor = it) }
                )
            }

            // Two independent sizes: one for app chrome (menus, buttons, labels),
            // one for the poem text itself.
            Label(text = stringResource(id = R.string.ui_font_size))
            Slider(
                value = draft.uiFontScale,
                onValueChange = { draftState = draft.copy(uiFontScale = it) },
                valueRange = 0.8f..1.6f,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Label(text = stringResource(id = R.string.poem_font_size))
            Slider(
                value = draft.poemFontScale,
                onValueChange = { draftState = draft.copy(poemFontScale = it) },
                valueRange = 0.8f..1.6f,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            ToggleRow(
                label = stringResource(id = R.string.italic),
                checked = draft.italic,
                onCheckedChange = { draftState = draft.copy(italic = it) }
            )
            ToggleRow(
                label = stringResource(id = R.string.underline),
                checked = draft.underline,
                onCheckedChange = { draftState = draft.copy(underline = it) }
            )
            ToggleRow(
                label = stringResource(id = R.string.strikethrough),
                checked = draft.strikethrough,
                onCheckedChange = { draftState = draft.copy(strikethrough = it) }
            )

            OutlinedTextField(
                value = customName,
                onValueChange = { customName = it },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.theme_name_hint),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )

            Button(
                onClick = {
                    val id = "custom_" + System.currentTimeMillis()
                    val name = customName.trim().ifEmpty { defaultCustomName }
                    viewModel.saveTheme(draft.copy(id = id, name = name, isCustom = true))
                    customName = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save_theme),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PoemSourcesSection(
    sources: List<GithubPoemSource>,
    onAddSource: suspend (owner: String, repo: String, branch: String) -> Boolean,
    onRemoveSource: (id: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var repoInput by remember { mutableStateOf("") }
    var branchInput by remember { mutableStateOf("main") }
    var isChecking by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        sources.forEach { source ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${source.owner}/${source.repo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (!source.isDefault) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onRemoveSource(source.id) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = repoInput,
                onValueChange = { repoInput = it; showError = false },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.source_repo_hint),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = branchInput,
                onValueChange = { branchInput = it },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.source_branch_hint),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.width(96.dp)
            )
        }

        Button(
            enabled = !isChecking,
            onClick = {
                val cleaned = repoInput.trim()
                    .removePrefix("https://github.com/")
                    .removePrefix("http://github.com/")
                    .trim('/')
                val parts = cleaned.split("/")
                if (parts.size != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    showError = true
                    return@Button
                }
                isChecking = true
                coroutineScope.launch {
                    val success = onAddSource(parts[0], parts[1], branchInput.trim().ifBlank { "main" })
                    isChecking = false
                    showError = !success
                    if (success) repoInput = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = if (isChecking) R.string.checking_source else R.string.add_source),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showError) {
            Text(
                text = stringResource(id = R.string.add_source_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/** Copies a picked image into app-private storage so it survives independent of the source Uri's lifetime. */
private suspend fun copyImageToInternalStorage(context: Context, uri: Uri, prefix: String): String? =
    withContext(Dispatchers.IO) {
        runCatching {
            val dir = File(context.filesDir, "theme_images").apply { mkdirs() }
            val file = File(dir, "${prefix}_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: return@runCatching null
            file.absolutePath
        }.getOrNull()
    }

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun cornerLabel(corner: ImageCorner): String = stringResource(
    id = when (corner) {
        ImageCorner.NONE -> R.string.corner_none
        ImageCorner.TOP_START -> R.string.corner_top_start
        ImageCorner.TOP_END -> R.string.corner_top_end
        ImageCorner.BOTTOM_START -> R.string.corner_bottom_start
        ImageCorner.BOTTOM_END -> R.string.corner_bottom_end
    }
)

@Composable
private fun languageLabel(language: AppLanguage): String = stringResource(
    id = when (language) {
        AppLanguage.SYSTEM -> R.string.language_system
        AppLanguage.ENGLISH -> R.string.language_english
        AppLanguage.SPANISH -> R.string.language_spanish
    }
)

@Composable
private fun fontLabel(font: AppFont): String = stringResource(
    id = when (font) {
        AppFont.DEFAULT -> R.string.font_default
        AppFont.SERIF -> R.string.font_serif
        AppFont.SANS -> R.string.font_sans
        AppFont.MONO -> R.string.font_mono
        AppFont.CASUAL -> R.string.font_casual
        AppFont.CONDENSED -> R.string.font_condensed
        AppFont.LIGHT -> R.string.font_light
        AppFont.BLACK -> R.string.font_black
    }
)

@Composable
private fun backgroundLabel(bg: PoemBackground): String = stringResource(
    id = when (bg) {
        PoemBackground.NONE -> R.string.background_none
        PoemBackground.PAPER -> R.string.background_paper
        PoemBackground.PAPYRUS -> R.string.background_papyrus
        PoemBackground.BLUEPRINT -> R.string.background_blueprint
        PoemBackground.CUSTOM_IMAGE -> R.string.background_custom_image
    }
)

@Composable
private fun themeDisplayName(config: ThemeConfig): String {
    if (config.isCustom) return config.name
    val res = when (config.id) {
        "dark" -> R.string.theme_dark
        "light" -> R.string.theme_light
        "sepia" -> R.string.theme_sepia
        "paper" -> R.string.theme_paper
        "papyrus" -> R.string.theme_papyrus
        "code" -> R.string.theme_code
        "terminal" -> R.string.theme_terminal
        "blueprint" -> R.string.theme_blueprint
        "comic" -> R.string.theme_comic
        else -> null
    }
    return if (res != null) stringResource(id = res) else config.name
}
