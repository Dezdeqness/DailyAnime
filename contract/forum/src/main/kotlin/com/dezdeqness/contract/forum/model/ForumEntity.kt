package com.dezdeqness.contract.forum.model

data class ForumEntity(
    val id: Long,
    val position: Long,
    val name: String,
    val permalink: String,
    val url: String,
)
