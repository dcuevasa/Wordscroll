package com.wordscroll.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wordscroll.core.settings.GithubPoemSource
import com.wordscroll.core.settings.GithubSourceDefaults
import com.wordscroll.core.settings.rawPoemsUrl
import com.wordscroll.data.di.SettingsDataStore
import com.wordscroll.data.remote.GithubRawApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubSourceRepository @Inject constructor(
    @SettingsDataStore private val dataStore: DataStore<Preferences>,
    private val githubRawApi: GithubRawApi,
    private val gson: Gson
) {
    private val sourcesKey = stringPreferencesKey("github_poem_sources")
    private val sourcesType = object : TypeToken<List<GithubPoemSource>>() {}.type

    /** The built-in default source is always present, first, and can't be removed. */
    val sources: Flow<List<GithubPoemSource>> = dataStore.data.map { prefs ->
        GithubSourceDefaults.default + decode(prefs[sourcesKey])
    }

    /**
     * Validates that `api/poemas.json` actually resolves for [owner]/[repoName]
     * before saving — a mistyped repo silently doing nothing would be
     * confusing. Returns false (and saves nothing) if the source can't be read.
     */
    suspend fun addSource(owner: String, repoName: String, branch: String): Boolean {
        val id = "$owner/$repoName"
        if (id == GithubSourceDefaults.POESIA_API_ES.id) return false
        val candidate = GithubPoemSource(id = id, owner = owner, repo = repoName, branch = branch)
        val isReachable = runCatching { githubRawApi.getPoems(candidate.rawPoemsUrl()) }.isSuccess
        if (!isReachable) return false

        dataStore.edit { prefs ->
            val current = decode(prefs[sourcesKey])
            if (current.any { it.id == id }) return@edit
            prefs[sourcesKey] = gson.toJson(current + candidate)
        }
        return true
    }

    suspend fun removeSource(id: String) {
        if (id == GithubSourceDefaults.POESIA_API_ES.id) return
        dataStore.edit { prefs ->
            prefs[sourcesKey] = gson.toJson(decode(prefs[sourcesKey]).filterNot { it.id == id })
        }
    }

    private fun decode(json: String?): List<GithubPoemSource> {
        if (json.isNullOrEmpty()) return emptyList()
        return gson.fromJson<List<GithubPoemSource>>(json, sourcesType) ?: emptyList()
    }
}
