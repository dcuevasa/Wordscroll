package com.wordscroll.domain.sources

import com.wordscroll.core.settings.GithubPoemSource
import com.wordscroll.data.repository.GithubSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGithubSourcesUseCase @Inject constructor(private val repo: GithubSourceRepository) {
    operator fun invoke(): Flow<List<GithubPoemSource>> = repo.sources
}

/** Returns false (without saving) if the repo doesn't have a readable `api/poemas.json`. */
class AddGithubSourceUseCase @Inject constructor(private val repo: GithubSourceRepository) {
    suspend operator fun invoke(owner: String, repoName: String, branch: String): Boolean =
        repo.addSource(owner, repoName, branch)
}

class RemoveGithubSourceUseCase @Inject constructor(private val repo: GithubSourceRepository) {
    suspend operator fun invoke(id: String) = repo.removeSource(id)
}
