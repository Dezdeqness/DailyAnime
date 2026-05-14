package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.contract.forum.repository.ForumRepository

class GetForumsUseCase(
    private val forumRepository: ForumRepository,
) {

    operator fun invoke(): Result<List<ForumEntity>> {
        val result = forumRepository.getForums()
        result.onFailure {
            return Result.failure(it)
        }

        val list = result.getOrDefault(emptyList())
            .filter { it.permalink !in HIDDEN_PERMALINKS }
            .sortedBy { it.position }

        return Result.success(list)
    }

    companion object {
        private val HIDDEN_PERMALINKS = setOf("hidden", "premoderation", "contests")
    }
}
