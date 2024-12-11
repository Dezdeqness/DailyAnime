package com.dezdeqness.data.provider

import com.dezdeqness.domain.model.TypeEntity

class HomeGenresProvider(
    private val configurationProvider: ConfigurationProvider,
) {

    fun getHomeSectionGenresIds() =
        getHomeSectionGenres().map { it.id }

    fun getHomeSectionGenres() =
        configurationProvider
            .getListGenre()
            .filter { it.type == TypeEntity.ANIME }
            .filter { item -> GENRE_ID_LIST.contains(item.id) }

    companion object {
        private const val GENRE_ID_ADVENTURE = "2"
        private const val GENRE_ID_ROMANTIC = "22"
        private const val GENRE_ID_SHOUNEN = "27"
        private val GENRE_ID_LIST = listOf(
            GENRE_ID_ADVENTURE,
            GENRE_ID_SHOUNEN,
            GENRE_ID_ROMANTIC,
        )
    }
}
