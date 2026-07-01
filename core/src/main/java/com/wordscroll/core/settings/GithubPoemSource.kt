package com.wordscroll.core.settings

/**
 * A GitHub repository used as a poem source. Any repo following the same
 * layout as the default one works: a `main`/other branch with
 * `api/poemas.json` (and, optionally, `api/poetas.json`) at its root,
 * served as raw files via raw.githubusercontent.com.
 */
data class GithubPoemSource(
    val id: String,
    val owner: String,
    val repo: String,
    val branch: String = "main",
    val isDefault: Boolean = false,
)

/** Raw GitHub URL for this source's poems file — no auth needed for public repos. */
fun GithubPoemSource.rawPoemsUrl(): String =
    "https://raw.githubusercontent.com/$owner/$repo/$branch/api/poemas.json"

object GithubSourceDefaults {
    val POESIA_API_ES = GithubPoemSource(
        id = "dcuevasa/poesia-api-es",
        owner = "dcuevasa",
        repo = "poesia-api-es",
        branch = "main",
        isDefault = true
    )

    val default = listOf(POESIA_API_ES)
}
