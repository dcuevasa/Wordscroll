package com.wordscroll.data.model

data class PoemModel(
    val title: String,
    val author: String,
    val lines: List<String>,
    val language: PoemLanguage = PoemLanguage.ENGLISH
) {
    val id: String = "${author.trim()}::${title.trim()}".lowercase()
    val lineCount: Int = lines.size
}

enum class PoemLanguage {
    ENGLISH,
    SPANISH
}
