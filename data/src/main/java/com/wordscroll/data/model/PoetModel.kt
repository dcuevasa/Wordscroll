package com.wordscroll.data.model

data class PoetModel(
    val name: String,
    val language: PoemLanguage,
    val poemCount: Int? = null
)
