package com.dezdeqness.domain.forum.usecases

import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.contract.forum.repository.ForumRepository
import com.dezdeqness.contract.forum.usecases.GetForumsUseCase
import javax.inject.Inject

class GetForumsUseCaseImpl @Inject constructor(
    private val forumRepository: ForumRepository,
) : GetForumsUseCase {

    override fun invoke(): Result<List<ForumEntity>> =
        forumRepository.getForums().map { forums -> forums.sortedBy { it.position } }
}
