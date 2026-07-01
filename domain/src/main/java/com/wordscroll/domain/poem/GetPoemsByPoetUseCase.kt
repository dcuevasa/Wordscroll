package com.wordscroll.domain.poem

import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.repository.PoemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoemsByPoetUseCase @Inject constructor(private val poemRepository: PoemRepository) {
    operator fun invoke(poetName: String): Flow<List<PoemModel>> {
        return poemRepository.getPoemsByAuthor(poetName)
    }
}
