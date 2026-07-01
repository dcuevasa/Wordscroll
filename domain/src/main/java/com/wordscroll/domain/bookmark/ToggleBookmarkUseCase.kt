package com.wordscroll.domain.bookmark

import com.wordscroll.data.model.PoemModel
import com.wordscroll.data.repository.BookmarkRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(private val bookmarkRepository: BookmarkRepository) {
    suspend operator fun invoke(poem: PoemModel) {
        bookmarkRepository.toggleBookmark(poem)
    }
}
