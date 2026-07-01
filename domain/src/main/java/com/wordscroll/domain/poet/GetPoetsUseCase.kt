package com.wordscroll.domain.poet

import com.wordscroll.data.model.PoetModel
import com.wordscroll.data.repository.PoemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoetsUseCase @Inject constructor(private val poemRepository: PoemRepository) {
    operator fun invoke(): Flow<List<PoetModel>> {
        return poemRepository.getAllPoets()
    }
}
