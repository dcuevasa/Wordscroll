package com.wordscroll.domain.poem

import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.repository.PoemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoemFeedUseCase @Inject constructor(private val poemRepository: PoemRepository) {
    operator fun invoke(count: Int = 10): Flow<List<PoemModel>> {
        return poemRepository.getFeed(count)
    }
}
