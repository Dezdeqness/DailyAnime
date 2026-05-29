package com.dezdeqness.feature.forum.presentation.preview

import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.shared.presentation.feature.topic.preview.TopicPreviewData

object ForumPreviewData {

    val sections = listOf(
        ForumUiModel(id = 1, name = "Аниме и манга", permalink = "animanga"),
        ForumUiModel(id = 2, name = "Новости", permalink = "news"),
        ForumUiModel(id = 3, name = "Сайт", permalink = "site"),
        ForumUiModel(id = 4, name = "Оффтопик", permalink = "offtopic"),
        ForumUiModel(id = 5, name = "Визуальные новеллы", permalink = "vn"),
        ForumUiModel(id = 6, name = "Игры", permalink = "games"),
    )

    val sectionItem = sections.first()

    val hotTopics = TopicPreviewData.hotTopics
}
