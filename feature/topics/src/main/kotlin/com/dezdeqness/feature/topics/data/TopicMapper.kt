package com.dezdeqness.feature.topics.data

import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.model.TopicLinkedBriefEntity
import com.dezdeqness.data.model.ImageRemote
import javax.inject.Inject

internal class TopicMapper @Inject constructor() {

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
        image = fromResponse(remote.image),
        url = remote.url,
    )

    private fun fromResponse(image: ImageRemote?) = ImageEntity(
        original = image?.original.orEmpty(),
        preview = image?.preview.orEmpty(),
        x96 = image?.x96.orEmpty(),
        x48 = image?.x48.orEmpty(),
    )
}
