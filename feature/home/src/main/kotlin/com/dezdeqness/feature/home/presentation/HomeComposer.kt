package com.dezdeqness.feature.home.presentation

import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.feature.home.presentation.models.SectionUiModel

class HomeComposer(
    private val homeGenresProvider: HomeGenresProvider,
) {

    suspend fun composeSectionsInitial(): SectionsState {
        val sections = homeGenresProvider.getHomeSectionGenres().map { genre ->
            SectionUiModel(
                id = genre.id,
                numericId = genre.numericId,
                title = genre.name,
            )
        }

        return SectionsState(genreSections = sections)
    }
}
