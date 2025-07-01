package com.dezdeqness.data.provider

import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.domain.model.TypeEntity

class HomeGenresProvider(
    private val configurationProvider: ConfigurationProvider,
    private val configManager: ConfigManager,
) {

    fun getHomeSectionGenresIds() =
        getHomeSectionGenres().map { it.numericId }

    fun getHomeSectionGenres() =
        configurationProvider
            .getListGenre()
            .filter { it.type == TypeEntity.ANIME }
            .filter { item -> configManager.homeGenresList.contains(item.numericId) }

}
