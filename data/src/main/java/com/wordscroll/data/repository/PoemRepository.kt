package com.wordscroll.data.repository

import com.wordscroll.core.settings.GithubPoemSource
import com.wordscroll.core.settings.rawPoemsUrl
import com.wordscroll.data.model.PoemLanguage
import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.model.PoetModel
import com.wordscroll.data.remote.GithubPoemDto
import com.wordscroll.data.remote.GithubRawApi
import com.wordscroll.data.remote.PoemDto
import com.wordscroll.data.remote.PoetryDbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoemRepository @Inject constructor(
    private val api: PoetryDbApi,
    private val githubRawApi: GithubRawApi,
    private val githubSourceRepository: GithubSourceRepository,
) {
    // Shared, app-scoped caches so a poet's poems are fetched once and reused
    // (e.g. the poet profile and the poem reader read the exact same list, in
    // the same order, with no second network round-trip).
    private val authorPoemsCache = ConcurrentHashMap<String, List<PoemModel>>()
    private val githubSourcePoemsCache = ConcurrentHashMap<String, List<PoemModel>>()

    @Volatile
    private var poetsCache: List<PoetModel>? = null

    fun getFeed(count: Int = 10): Flow<List<PoemModel>> = flow {
        val poetryDbPoems = runCatching { api.getRandomPoems(count).map { it.toPoemModel() } }
            .getOrDefault(emptyList())
        val githubPoems = allGithubSourcePoems()
        // One failing source shouldn't zero out poems the others already provided.
        emit((poetryDbPoems + githubPoems).shuffled())
    }.catch { emit(emptyList()) }

    fun getPoemsByAuthor(author: String): Flow<List<PoemModel>> = flow {
        authorPoemsCache[author]?.let { emit(it); return@flow }
        val poetryDbPoems = runCatching { api.getPoemsByAuthor(author).map { it.toPoemModel() } }
            .getOrDefault(emptyList())
        val githubPoems = allGithubSourcePoems().filter { it.author.equals(author, ignoreCase = true) }
        val combined = poetryDbPoems + githubPoems
        authorPoemsCache[author] = combined
        emit(combined)
    }.catch { emit(authorPoemsCache[author] ?: emptyList()) }

    fun getAllPoets(): Flow<List<PoetModel>> = flow {
        poetsCache?.let { emit(it); return@flow }
        val poetryDbPoets = runCatching {
            api.getAuthors().authors.map { name -> PoetModel(name = name, language = PoemLanguage.ENGLISH) }
        }.getOrDefault(emptyList())
        // Poet counts are derived from the poems actually available, so they
        // can never drift from what a poet's profile will actually show.
        val githubPoets = allGithubSourcePoems()
            .groupBy { it.author }
            .map { (author, poems) -> PoetModel(name = author, language = poems.first().language, poemCount = poems.size) }
        val merged = (poetryDbPoets + githubPoets).distinctBy { it.name.lowercase() }.sortedBy { it.name }
        poetsCache = merged
        emit(merged)
    }.catch { emit(poetsCache ?: emptyList()) }

    private suspend fun allGithubSourcePoems(): List<PoemModel> =
        githubSourceRepository.sources.first().flatMap { fetchGithubSourcePoems(it) }

    private suspend fun fetchGithubSourcePoems(source: GithubPoemSource): List<PoemModel> {
        githubSourcePoemsCache[source.id]?.let { return it }
        val poems = runCatching {
            githubRawApi.getPoems(source.rawPoemsUrl()).map { it.toPoemModel() }
        }.getOrDefault(emptyList())
        if (poems.isNotEmpty()) githubSourcePoemsCache[source.id] = poems
        return poems
    }
}

private fun PoemDto.toPoemModel() = PoemModel(
    title = title,
    author = author,
    lines = lines,
    language = PoemLanguage.ENGLISH
)

private fun GithubPoemDto.toPoemModel() = PoemModel(
    title = title,
    author = author,
    lines = lines,
    language = PoemLanguage.values().firstOrNull { it.name.equals(language, ignoreCase = true) }
        ?: PoemLanguage.SPANISH
)
