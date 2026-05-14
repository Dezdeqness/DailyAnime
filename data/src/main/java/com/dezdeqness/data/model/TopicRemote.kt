package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class TopicRemote(
    val id: Long,
    @field:Json("topic_title")
    val topicTitle: String,
    val body: String,
    @field:Json("html_body")
    val htmlBody: String,
    @field:Json("html_footer")
    val htmlFooter: String?,
    @field:Json("created_at")
    val createdAt: String,
    @field:Json("comments_count")
    val commentsCount: Long,
    val forum: ForumRemote,
    val user: UserRemote,
    val type: String,
    @field:Json("linked_id")
    val linkedId: Long?,
    @field:Json("linked_type")
    val linkedType: String?,
    val linked: TopicLinkedBriefRemote?,
    val viewed: Boolean,
    @field:Json("last_comment_viewed")
    val lastCommentViewed: Boolean?,
    val event: String?,
    val episode: Int?,
) : DataModel.Api
