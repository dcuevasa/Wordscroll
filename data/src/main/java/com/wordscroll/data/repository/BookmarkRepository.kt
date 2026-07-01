package com.wordscroll.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wordscroll.data.di.BookmarkDataStore
import com.wordscroll.data.model.PoemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    @BookmarkDataStore private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {
    private val bookmarksKey = stringPreferencesKey("bookmarked_poems")
    private val listType = object : TypeToken<List<PoemModel>>() {}.type

    fun getBookmarks(): Flow<List<PoemModel>> = dataStore.data.map { prefs ->
        decode(prefs[bookmarksKey])
    }

    suspend fun toggleBookmark(poem: PoemModel) {
        dataStore.edit { prefs ->
            val current = decode(prefs[bookmarksKey])
            val isBookmarked = current.any { it.id == poem.id }
            val updated = if (isBookmarked) current.filterNot { it.id == poem.id } else current + poem
            prefs[bookmarksKey] = gson.toJson(updated)
        }
    }

    private fun decode(json: String?): List<PoemModel> {
        if (json.isNullOrEmpty()) return emptyList()
        return gson.fromJson<List<PoemModel>>(json, listType) ?: emptyList()
    }
}
