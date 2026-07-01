package com.wordscroll.data.repository

import com.wordscroll.data.model.PoemLanguage
import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.model.PoetModel
import com.wordscroll.data.remote.PoemDto
import com.wordscroll.data.remote.PoetryDbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PoemRepository @Inject constructor(
    private val api: PoetryDbApi
) {
    fun getFeed(count: Int = 10): Flow<List<PoemModel>> = flow {
        emit(api.getRandomPoems(count).map { it.toPoemModel() })
    }.catch { emit(emptyList()) }

    fun getPoemsByAuthor(author: String): Flow<List<PoemModel>> = flow {
        emit(api.getPoemsByAuthor(author).map { it.toPoemModel() })
    }.catch { emit(emptyList()) }

    fun getAllPoets(): Flow<List<PoetModel>> = flow {
        val poets = api.getAuthors().authors.map { name ->
            PoetModel(name = name, language = PoemLanguage.ENGLISH)
        }
        emit(poets)
    }.catch { emit(emptyList()) }
}

private fun PoemDto.toPoemModel() = PoemModel(
    title = title,
    author = author,
    lines = lines,
    language = PoemLanguage.ENGLISH
)
