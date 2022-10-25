package com.dezdeqness.domain.model

data class AnimeDetailsFullEntity(
    val animeDetailsEntity: AnimeDetailsEntity,
    val screenshots: List<ScreenshotEntity>,
    val relates: List<RelatedItemEntity>,
    val roles: List<RoleEntity>,
)
