package com.wordscroll.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Fetches poems directly from a GitHub repo's raw content — a different repo
 * per call, so every method takes the full URL rather than a fixed path.
 */
interface GithubRawApi {
    @GET
    suspend fun getPoems(@Url url: String): List<GithubPoemDto>

    companion object {
        const val BASE_URL = "https://raw.githubusercontent.com/"
    }
}
