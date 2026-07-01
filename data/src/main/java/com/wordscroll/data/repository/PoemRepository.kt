package com.wordscroll.data.repository

import com.wordscroll.data.model.PoemLanguage
import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.model.PoetModel
import com.wordscroll.data.remote.PoemDto
import com.wordscroll.data.remote.PoetryDbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoemRepository @Inject constructor(
    private val api: PoetryDbApi
) {
    // Shared, app-scoped caches so a poet's poems are fetched once and reused
    // (e.g. the poet profile and the poem reader read the exact same list, in
    // the same order, with no second network round-trip).
    private val authorPoemsCache = ConcurrentHashMap<String, List<PoemModel>>()

    @Volatile
    private var poetsCache: List<PoetModel>? = null

    fun getFeed(count: Int = 10): Flow<List<PoemModel>> = flow {
        emit(api.getRandomPoems(count).map { it.toPoemModel() })
    }.catch { emit(emptyList()) }

    fun getPoemsByAuthor(author: String): Flow<List<PoemModel>> = flow {
        authorPoemsCache[author]?.let { emit(it); return@flow }
        val poems = api.getPoemsByAuthor(author).map { it.toPoemModel() }
        authorPoemsCache[author] = poems
        emit(poems)
    }.catch { emit(authorPoemsCache[author] ?: emptyList()) }

    fun getAllPoets(): Flow<List<PoetModel>> = flow {
        poetsCache?.let { emit(it); return@flow }
        val poets = api.getAuthors().authors.map { name ->
            PoetModel(name = name, language = PoemLanguage.ENGLISH)
        }
        poetsCache = poets
        emit(poets)
    }.catch { emit(poetsCache ?: emptyList()) }
}

private fun PoemDto.toPoemModel() = PoemModel(
    title = title,
    author = author,
    lines = lines,
    language = PoemLanguage.ENGLISH
)
