package com.dezdeqness.contract.topic.model

import com.dezdeqness.contract.anime.model.ImageEntity

data class TopicLinkedBriefEntity(
    val id: Long,
    val name: String,
    val russian: String,
    val image: ImageEntity,
    val url: String,
)
