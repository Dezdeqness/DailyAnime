package com.dezdeqness.shared.presentation.feature.topic.model

data class TopicPresentationModel(
    val topicId: Long,
    val title: String,
    val userNickname: String,
    val userAvatarUrl: String,
    val date: String,
    val commentsCount: Long,
    val contentBlocks: List<ParagraphBlock>,
    val footerBlocks: List<ParagraphBlock>,
    val linkedTitle: String? = null,
    val linkedImageUrl: String? = null,
    val linkedId: Long? = null,
    val linkedType: String? = null,
    val linkedUrl: String? = null,
)
