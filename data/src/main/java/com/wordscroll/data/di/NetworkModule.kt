package com.wordscroll.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.data.remote.GithubRawApi
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookmarkDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStore

private val Context.bookmarkDataStoreImpl: DataStore<Preferences> by preferencesDataStore(name = "wordscroll_bookmarks")
private val Context.settingsDataStoreImpl: DataStore<Preferences> by preferencesDataStore(name = "wordscroll_settings")

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        // Gson normally allocates Kotlin data classes via unsafe reflection,
        // skipping the constructor entirely — any field missing from an
        // older saved JSON (e.g. a field added in a later app version) comes
        // back as a raw zero/null instead of its declared Kotlin default.
        // Seeding the instance through the real constructor first means only
        // fields actually present in the JSON get overwritten; everything
        // else keeps its proper default.
        .registerTypeAdapter(
            ThemeConfig::class.java,
            InstanceCreator<ThemeConfig> { ThemeConfig(id = "", name = "") }
        )
        .create()

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
    fun provideGithubRawApi(okHttpClient: OkHttpClient, gson: Gson): GithubRawApi {
        return Retrofit.Builder()
            .baseUrl(GithubRawApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GithubRawApi::class.java)
    }

    @Provides
    @Singleton
    @BookmarkDataStore
    fun provideBookmarkDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.bookmarkDataStoreImpl
    }

    @Provides
    @Singleton
    @SettingsDataStore
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.settingsDataStoreImpl
    }
}
