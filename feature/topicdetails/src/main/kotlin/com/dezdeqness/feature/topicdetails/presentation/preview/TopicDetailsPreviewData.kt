package com.dezdeqness.feature.topicdetails.presentation.preview

import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityState
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel
import com.dezdeqness.shared.presentation.feature.topic.preview.TopicPreviewData

object TopicDetailsPreviewData {

    val topic = TopicPreviewData.detailedTopic

    val linkedEntityCard = LinkedEntityUiModel.Anime(
        id = 16498,
        imageUrl = "",
        title = "Атака титанов: Финал",
        status = "Вышло",
        type = "TV Сериал",
    )

    val linkedEntityState = LinkedEntityState.Loaded(entity = linkedEntityCard)
}
