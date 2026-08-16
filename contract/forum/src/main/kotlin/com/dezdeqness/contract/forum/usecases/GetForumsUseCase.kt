package com.dezdeqness.contract.forum.usecases

import com.dezdeqness.contract.forum.model.ForumEntity

interface GetForumsUseCase {

    operator fun invoke(): Result<List<ForumEntity>>
}
