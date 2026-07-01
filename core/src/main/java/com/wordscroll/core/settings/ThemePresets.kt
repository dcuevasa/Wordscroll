package com.wordscroll.core.settings

/** Built-in themes. [id] is stable and used to persist the current selection. */
object ThemePresets {

    val DARK = ThemeConfig(
        id = "dark", name = "Dark",
        font = AppFont.SERIF,
        backgroundColor = 0xFF14130F, textColor = 0xFFEDEAE3, accentColor = 0xFFB0623B,
        darkSystemIcons = false
    )

    val LIGHT = ThemeConfig(
        id = "light", name = "Light",
        font = AppFont.SERIF,
        backgroundColor = 0xFFFBF8F3, textColor = 0xFF1C1B1A, accentColor = 0xFFB0623B,
        darkSystemIcons = true
    )

    val SEPIA = ThemeConfig(
        id = "sepia", name = "Sepia",
        font = AppFont.SERIF,
        backgroundColor = 0xFFF4ECD8, textColor = 0xFF5B4636, accentColor = 0xFFA0552B,
        darkSystemIcons = true
    )

    val PAPER = ThemeConfig(
        id = "paper", name = "Paper",
        font = AppFont.SERIF, background = PoemBackground.PAPER,
        backgroundColor = 0xFFFAF3E0, textColor = 0xFF2E2A22, accentColor = 0xFF9C6B3F,
        darkSystemIcons = true
    )

    val PAPYRUS = ThemeConfig(
        id = "papyrus", name = "Papyrus",
        font = AppFont.SERIF, background = PoemBackground.PAPYRUS,
        backgroundColor = 0xFFD9C3A5, textColor = 0xFF4A3520, accentColor = 0xFF7B5230,
        italic = true, darkSystemIcons = true
    )

    val PROGRAMMER = ThemeConfig(
        id = "code", name = "Highlighted",
        font = AppFont.MONO,
        backgroundColor = 0xFF0D1117, textColor = 0xFF7EE787, accentColor = 0xFF58A6FF,
        darkSystemIcons = false
    )

    val TERMINAL = ThemeConfig(
        id = "terminal", name = "Terminal",
        font = AppFont.MONO,
        backgroundColor = 0xFF000000, textColor = 0xFF33FF66,
        // Bright cyan accent — white-on-cyan barely reads, so this one needs dark on-accent text.
        accentColor = 0xFF00E5FF, onAccentColor = 0xFF14130F,
        darkSystemIcons = false
    )

    val BLUEPRINT = ThemeConfig(
        id = "blueprint", name = "Blueprint",
        font = AppFont.MONO, background = PoemBackground.BLUEPRINT,
        backgroundColor = 0xFF0B3D91, textColor = 0xFFE6F0FF,
        // Pale accent — same reasoning as Terminal above.
        accentColor = 0xFF9FC3FF, onAccentColor = 0xFF0B3D91,
        darkSystemIcons = false
    )

    val COMIC = ThemeConfig(
        id = "comic", name = "Comic",
        font = AppFont.CASUAL,
        backgroundColor = 0xFFFFF3B0, textColor = 0xFF2B2D42, accentColor = 0xFFEF476F,
        uiFontScale = 1.05f, darkSystemIcons = true
    )

    val all = listOf(DARK, LIGHT, SEPIA, PAPER, PAPYRUS, PROGRAMMER, TERMINAL, BLUEPRINT, COMIC)
    val default = DARK

    fun byId(id: String?): ThemeConfig = all.firstOrNull { it.id == id } ?: default
}
