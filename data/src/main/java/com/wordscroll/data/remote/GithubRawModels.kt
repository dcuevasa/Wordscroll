package com.wordscroll.data.remote

data class GithubPoemDto(
    val title: String,
    val author: String,
    val lines: List<String>,
    val language: String? = null
)
