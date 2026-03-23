package com.dezdeqness.contract.topic.model

import com.dezdeqness.contract.anime.model.AnimeBriefEntity

data class TopicEntity(
    val id: Long,
    val topicTitle: String,
    val body: String,
    val htmlBody: String,
    val htmlFooter: String,
    val createdAt: String,
    val commentsCount: Long,
    val type: String,
    val forumName: String,
    val userNickname: String,
    val userAvatarUrl: String,
    val linkedId: Long?,
    val linkedType: String,
    val linked: AnimeBriefEntity?,
    val viewed: Boolean,
    val lastCommentViewed: Boolean?,
    val event: String?,
    val episode: Int?,
)
