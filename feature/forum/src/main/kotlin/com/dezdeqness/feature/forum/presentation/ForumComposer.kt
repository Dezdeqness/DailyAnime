package com.dezdeqness.feature.forum.presentation

import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import javax.inject.Inject

class ForumComposer @Inject constructor() {

    fun compose(forums: List<ForumEntity>): List<ForumUiModel> =
        forums.map { forum ->
            ForumUiModel(
                id = forum.id,
                name = forum.name,
                permalink = forum.permalink,
            )
        }
}
