package com.wordscroll.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class PoetModel(
    val name: String,
    val language: PoemLanguage,
    val poemCount: Int? = null
)
