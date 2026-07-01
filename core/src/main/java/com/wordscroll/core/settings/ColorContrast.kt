package com.wordscroll.core.settings

/** Perceived-luminance check so callers can pick dark-on-light vs light-on-dark defaults. */
fun isLightColor(argb: Long): Boolean {
    val r = ((argb shr 16) and 0xFFL).toDouble()
    val g = ((argb shr 8) and 0xFFL).toDouble()
    val b = (argb and 0xFFL).toDouble()
    return (0.299 * r + 0.587 * g + 0.114 * b) / 255.0 > 0.6
}

/**
 * A readable default color for text/icons drawn on top of [background] — used
 * to auto-pick a sane default the moment a background/accent color is chosen.
 * Always overridable afterwards via its own swatch.
 */
fun contrastTextColor(background: Long): Long =
    if (isLightColor(background)) 0xFF1C1B1A else 0xFFEDEAE3
