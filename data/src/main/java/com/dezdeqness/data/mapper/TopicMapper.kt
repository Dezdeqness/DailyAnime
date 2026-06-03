package com.dezdeqness.data.mapper

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.model.TopicLinkedBriefEntity
import com.dezdeqness.data.model.TopicLinkedBriefRemote
import com.dezdeqness.data.model.TopicRemote
import javax.inject.Inject

class TopicMapper @Inject constructor(
    private val imageMapper: ImageMapper,
) {

    fun fromResponse(remote: TopicRemote): TopicEntity {
        val linked = remote.linked
        return TopicEntity(
            id = remote.id,
            topicTitle = remote.topicTitle,
            body = remote.body,
            htmlBody = remote.htmlBody,
            htmlFooter = remote.htmlFooter.orEmpty(),
            createdAt = remote.createdAt,
            commentsCount = remote.commentsCount,
            type = remote.type,
            forumName = remote.forum.name,
            userNickname = remote.user.nickname,
            userAvatarUrl = remote.user.avatar,
            linkedId = remote.linkedId,
            linkedType = remote.linkedType,
            linked = if (linked != null) fromResponse(linked) else null,
            viewed = remote.viewed,
            lastCommentViewed = remote.lastCommentViewed,
            event = remote.event,
            episode = remote.episode,
        )
    }

    fun fromResponse(remote: TopicLinkedBriefRemote): TopicLinkedBriefEntity = TopicLinkedBriefEntity(
        id = remote.id,
        name = remote.name,
        russian = remote.russian,
        image = imageMapper.fromResponse(remote.image),
        url = remote.url,
    )
}
