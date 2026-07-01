package com.wordscroll.core.utils

import android.content.Context
import android.content.Intent

object IntentUtils {
    fun Context.share(
        type: String = "text/plain",
        title: String = "",
        text: String = ""
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            setType(type)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(intent, title)
        startActivity(chooserIntent)
    }
}
