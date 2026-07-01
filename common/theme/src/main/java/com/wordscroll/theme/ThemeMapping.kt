package com.wordscroll.theme

import android.graphics.BitmapFactory
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.isSpecified
import com.wordscroll.core.settings.AppFont
import com.wordscroll.core.settings.ImageCorner
import com.wordscroll.core.settings.PoemBackground
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.ThemePresets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** The active theme configuration, available to any composable in the tree. */
val LocalAppTheme = staticCompositionLocalOf { ThemePresets.default }

/**
 * All font families resolve to something already on the device — Android's
 * generic families plus its standard system aliases — so no font is ever
 * downloaded or bundled with the app.
 */
fun AppFont.toFontFamily(): FontFamily = when (this) {
    AppFont.DEFAULT -> fontFamily
    AppFont.SERIF -> FontFamily.Serif
    AppFont.SANS -> FontFamily.SansSerif
    AppFont.MONO -> FontFamily.Monospace
    AppFont.CASUAL -> FontFamily.Cursive
    AppFont.CONDENSED -> FontFamily(Font(DeviceFontFamilyName("sans-serif-condensed")))
    AppFont.LIGHT -> FontFamily(Font(DeviceFontFamilyName("sans-serif-light")))
    AppFont.BLACK -> FontFamily(Font(DeviceFontFamilyName("sans-serif-black")))
}

fun ThemeConfig.textDecoration(): TextDecoration = when {
    underline && strikethrough ->
        TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
    underline -> TextDecoration.Underline
    strikethrough -> TextDecoration.LineThrough
    else -> TextDecoration.None
}

fun ThemeConfig.colorScheme() = if (darkSystemIcons) {
    lightColorScheme(
        primary = Color(accentColor),
        onPrimary = Color(onAccentColor),
        secondary = Color(accentColor),
        onSecondary = Color(onAccentColor),
        background = Color(backgroundColor),
        onBackground = Color(effectiveUiTextColor),
        surface = Color(backgroundColor),
        surfaceTint = Color(backgroundColor),
        onSurface = Color(effectiveUiTextColor),
    )
} else {
    darkColorScheme(
        primary = Color(accentColor),
        onPrimary = Color(onAccentColor),
        secondary = Color(accentColor),
        onSecondary = Color(onAccentColor),
        background = Color(backgroundColor),
        onBackground = Color(effectiveUiTextColor),
        surface = Color(backgroundColor),
        surfaceTint = Color(backgroundColor),
        onSurface = Color(effectiveUiTextColor),
    )
}

/** Scales only app-chrome typography (menus, buttons, labels) — never the poem text itself. */
private fun TextStyle.applyTheme(config: ThemeConfig): TextStyle = copy(
    fontFamily = config.font.toFontFamily(),
    fontStyle = if (config.italic) FontStyle.Italic else fontStyle,
    textDecoration = config.textDecoration(),
    fontSize = if (fontSize.isSpecified) fontSize * config.uiFontScale else fontSize,
    lineHeight = if (lineHeight.isSpecified) lineHeight * config.uiFontScale else lineHeight,
)

fun ThemeConfig.typography(base: Typography = Typography): Typography = Typography(
    displayLarge = base.displayLarge.applyTheme(this),
    displayMedium = base.displayMedium.applyTheme(this),
    displaySmall = base.displaySmall.applyTheme(this),
    headlineLarge = base.headlineLarge.applyTheme(this),
    headlineMedium = base.headlineMedium.applyTheme(this),
    headlineSmall = base.headlineSmall.applyTheme(this),
    titleLarge = base.titleLarge.applyTheme(this),
    titleMedium = base.titleMedium.applyTheme(this),
    titleSmall = base.titleSmall.applyTheme(this),
    bodyLarge = base.bodyLarge.applyTheme(this),
    bodyMedium = base.bodyMedium.applyTheme(this),
    bodySmall = base.bodySmall.applyTheme(this),
    labelLarge = base.labelLarge.applyTheme(this),
    labelMedium = base.labelMedium.applyTheme(this),
    labelSmall = base.labelSmall.applyTheme(this),
)

@Composable
private fun PoemBackground.presetPainterOrNull(): Painter? = when (this) {
    PoemBackground.NONE, PoemBackground.CUSTOM_IMAGE -> null
    PoemBackground.PAPER -> painterResource(R.drawable.bg_paper)
    PoemBackground.PAPYRUS -> painterResource(R.drawable.bg_papyrus)
    PoemBackground.BLUEPRINT -> painterResource(R.drawable.bg_blueprint)
}

/** Kept for call sites that only care about built-in (resource) backgrounds. */
@Composable
fun PoemBackground.painterOrNull(): Painter? = presetPainterOrNull()

/** Decodes a local image file off the main thread; returns null while loading or on failure. */
@Composable
private fun rememberFileImagePainter(path: String?): Painter? {
    if (path == null) return null
    val bitmapState = produceState<Painter?>(initialValue = null, key1 = path) {
        value = withContext(Dispatchers.IO) {
            runCatching { BitmapFactory.decodeFile(path) }
                .getOrNull()
                ?.let { BitmapPainter(it.asImageBitmap()) }
        }
    }
    return bitmapState.value
}

/**
 * Resolves the full-bleed background to draw for [config]: a built-in
 * resource, a user-uploaded image decoded from local storage, or nothing.
 */
@Composable
fun ThemeConfig.rememberBackgroundPainter(): Painter? {
    if (background != PoemBackground.CUSTOM_IMAGE) return background.presetPainterOrNull()
    return rememberFileImagePainter(customBackgroundPath)
}

/** Resolves the small corner "companion" image for [config], if one is set and enabled. */
@Composable
fun ThemeConfig.rememberCompanionImagePainter(): Painter? {
    if (companionImageCorner == ImageCorner.NONE) return null
    return rememberFileImagePainter(companionImagePath)
}
