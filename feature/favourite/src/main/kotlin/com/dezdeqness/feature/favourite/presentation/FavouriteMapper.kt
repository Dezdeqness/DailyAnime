package com.dezdeqness.feature.favourite.presentation

import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel
import javax.inject.Inject

class FavouriteMapper @Inject constructor(
    private val configManager: ConfigManager,
) {

    fun map(item: FavouriteEntity) = FavouritesUiModel(
        id = item.id.toString(),
        title = item.name.ifEmpty { item.russian.orEmpty() },
        imageUrl = configManager.baseUrl + item.image?.replace("x64", "original"),
    )
}
