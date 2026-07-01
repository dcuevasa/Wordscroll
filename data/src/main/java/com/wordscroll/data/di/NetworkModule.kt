package com.wordscroll.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.wordscroll.data.remote.PoetryDbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private val Context.bookmarkDataStore: DataStore<Preferences> by preferencesDataStore(name = "wordscroll_bookmarks")

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun providePoetryDbApi(okHttpClient: OkHttpClient, gson: Gson): PoetryDbApi {
        return Retrofit.Builder()
            .baseUrl(PoetryDbApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PoetryDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.bookmarkDataStore
    }
}
