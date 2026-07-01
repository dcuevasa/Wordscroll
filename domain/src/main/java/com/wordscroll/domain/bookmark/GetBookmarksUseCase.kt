package com.wordscroll.domain.bookmark

import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(private val bookmarkRepository: BookmarkRepository) {
    operator fun invoke(): Flow<List<PoemModel>> {
        return bookmarkRepository.getBookmarks()
    }
}
