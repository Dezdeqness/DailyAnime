package com.dezdeqness.data.mapper

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.data.model.TopicRemote
import javax.inject.Inject

class TopicMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
) {

    fun fromResponse(remote: TopicRemote): TopicEntity {
        return TopicEntity(
            id = remote.id,
            topicTitle = remote.topicTitle,
            body = remote.body,
            htmlBody = remote.htmlBody,
            htmlFooter = remote.htmlFooter,
            createdAt = remote.createdAt,
            commentsCount = remote.commentsCount,
            type = remote.type,
            forumName = remote.forum.name,
            userNickname = remote.user.nickname,
            userAvatarUrl = remote.user.avatar,
            linkedId = remote.linkedId,
            linkedType = remote.linkedType,
            linked = remote.linked?.let { animeMapper.fromResponse(it) },
            viewed = remote.viewed,
            lastCommentViewed = remote.lastCommentViewed,
            event = remote.event,
            episode = remote.episode,
        )
    }

}
