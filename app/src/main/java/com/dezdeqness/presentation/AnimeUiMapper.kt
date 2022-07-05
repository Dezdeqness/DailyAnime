package com.dezdeqness.presentation

import com.dezdeqness.domain.AnimeEntity
import com.dezdeqness.presentation.models.AnimeUiItem
import javax.inject.Inject

class AnimeUiMapper @Inject constructor() {

    fun map(animeEntity: AnimeEntity) = AnimeUiItem

}
