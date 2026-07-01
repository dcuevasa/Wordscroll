package com.wordscroll.core.settings

import androidx.compose.runtime.Immutable

/**
 * UI language. Adding a new language = add an entry here + a matching
 * values-<tag>/strings.xml. [SYSTEM] follows the device locale.
 */
enum class AppLanguage(val tag: String?) {
    SYSTEM(null),
    ENGLISH("en"),
    SPANISH("es");

    companion object {
        fun fromTag(tag: String?): AppLanguage =
            values().firstOrNull { !it.tag.isNullOrEmpty() && it.tag == tag } ?: SYSTEM
    }
}

/**
 * Font family choices. All map to fonts already present on the device (Android's
 * generic font families / standard system aliases) — nothing is downloaded or bundled.
 */
enum class AppFont {
    DEFAULT,    // app default (Proxima Nova)
    SERIF,
    SANS,
    MONO,
    CASUAL,     // playful / handwritten
    CONDENSED,  // sans-serif-condensed
    LIGHT,      // sans-serif-light
    BLACK       // sans-serif-black (bold, dramatic)
}

/** Optional textured background drawn behind the poem. */
enum class PoemBackground {
    NONE,
    PAPER,
    PAPYRUS,
    BLUEPRINT,
    CUSTOM_IMAGE
}

/** Where the companion image sits on the poem screen. [NONE] hides it. */
enum class ImageCorner {
    NONE,
    TOP_START,
    TOP_END,
    BOTTOM_START,
    BOTTOM_END
}

/**
 * A complete, serializable description of a theme. Colors are stored as ARGB
 * longs so the model stays free of any Compose/Android color type and can be
 * persisted as plain JSON.
 *
 * Font size is split in two: [uiFontScale] scales app chrome (menus, buttons,
 * labels) while [poemFontScale] scales only the poem title/lines, so a reader
 * can bump up poem text without also blowing up the navigation UI.
 *
 * Text color is also split: [textColor] is always the poem's text color. App
 * chrome (menus, labels, buttons) uses [textColor] too *unless*
 * [useSeparateUiTextColor] is on, in which case it uses [uiTextColor] instead —
 * useful since the app's menus are drawn on a solid [backgroundColor] while a
 * poem page may sit on a custom image, so one color doesn't always suit both.
 */
@Immutable
data class ThemeConfig(
    val id: String,
    val name: String,
    val font: AppFont = AppFont.SERIF,
    val background: PoemBackground = PoemBackground.NONE,
    val customBackgroundPath: String? = null,
    val companionImagePath: String? = null,
    val companionImageCorner: ImageCorner = ImageCorner.NONE,
    val backgroundColor: Long = 0xFF14130F,
    val textColor: Long = 0xFFEDEAE3,
    val useSeparateUiTextColor: Boolean = false,
    val uiTextColor: Long = 0xFFEDEAE3,
    val accentColor: Long = 0xFFB0623B,
    val onAccentColor: Long = 0xFFFFFFFF,
    val uiFontScale: Float = 1f,
    val poemFontScale: Float = 1f,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strikethrough: Boolean = false,
    val darkSystemIcons: Boolean = false,
    val isCustom: Boolean = false
) {
    /** The color app chrome (menus/labels/buttons) actually renders text with. */
    val effectiveUiTextColor: Long get() = if (useSeparateUiTextColor) uiTextColor else textColor
}
