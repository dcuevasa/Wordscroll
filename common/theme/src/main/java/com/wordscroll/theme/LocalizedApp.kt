package com.wordscroll.theme

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * A context that keeps the original (Activity) context in its base chain — so
 * `findActivity()` used by hiltViewModel() still works — while returning
 * resources resolved against the chosen locale.
 */
private class LocalizedContextWrapper(
    base: Context,
    private val localizedResources: Resources
) : ContextWrapper(base) {
    override fun getResources(): Resources = localizedResources
}

/**
 * Overrides the locale used to resolve string resources for the whole subtree,
 * so the UI language can be switched at runtime without recreating the Activity.
 * A null/blank [languageTag] falls back to the device locale.
 */
@Composable
fun LocalizedApp(languageTag: String?, content: @Composable () -> Unit) {
    val context = LocalContext.current

    if (languageTag.isNullOrEmpty()) {
        content()
        return
    }

    val localized = remember(languageTag, context) {
        val locale = Locale.forLanguageTag(languageTag)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        val resources = context.createConfigurationContext(configuration).resources
        LocalizedContextWrapper(context, resources) to configuration
    }

    CompositionLocalProvider(
        LocalContext provides localized.first,
        LocalConfiguration provides localized.second
    ) {
        content()
    }
}
