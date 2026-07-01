package com.wordscroll.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface PoetryDbApi {
    @GET("random/{count}")
    suspend fun getRandomPoems(@Path("count") count: Int): List<PoemDto>

    @GET("author/{author}")
    suspend fun getPoemsByAuthor(@Path("author") author: String): List<PoemDto>

    @GET("author")
    suspend fun getAuthors(): AuthorsDto

    companion object {
        const val BASE_URL = "https://poetrydb.org/"
    }
}
