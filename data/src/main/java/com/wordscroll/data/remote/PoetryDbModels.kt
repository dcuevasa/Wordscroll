package com.wordscroll.data.remote

import com.google.gson.annotations.SerializedName

data class PoemDto(
    val title: String,
    val author: String,
    val lines: List<String>,
    @SerializedName("linecount") val lineCount: String
)

data class AuthorsDto(
    val authors: List<String>
)
