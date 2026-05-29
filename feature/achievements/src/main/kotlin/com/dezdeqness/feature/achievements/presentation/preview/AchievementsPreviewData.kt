package com.dezdeqness.feature.achievements.presentation.preview

import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel

object AchievementsPreviewData {

    val cell = AchievementsUiModel(
        id = "1",
        titleRu = "Любитель аниме",
        titleEn = "Anime Fan",
        level = 3,
        progress = 0.65f,
        progressValue = 65,
        imageUrl = "",
    )

    val commonList = listOf(
        AchievementsUiModel(
            id = "1",
            titleRu = "Любитель аниме",
            titleEn = "Anime Fan",
            level = 3,
            progress = 0.65f,
            progressValue = 65,
            imageUrl = "",
        ),
        AchievementsUiModel(
            id = "2",
            titleRu = "Отаку",
            titleEn = "Otaku",
            level = 5,
            progress = 0.8f,
            progressValue = 80,
            imageUrl = "",
        ),
    )

    val genresList = listOf(
        AchievementsUiModel(
            id = "3",
            titleRu = "Фантастика",
            titleEn = "Sci-Fi",
            level = 2,
            progress = 0.4f,
            progressValue = 40,
            imageUrl = "",
        ),
        AchievementsUiModel(
            id = "4",
            titleRu = "Романтика",
            titleEn = "Romance",
            level = 1,
            progress = 0.15f,
            progressValue = 15,
            imageUrl = "",
        ),
    )
}
