package com.dezdeqness.feature.home.presentation

import com.dezdeqness.contract.settings.repository.UserInterestsProvider
import com.dezdeqness.feature.home.presentation.models.SectionUiModel

class HomeComposer(
    private val userInterestsProvider: UserInterestsProvider,
) {

    suspend fun composeSectionsInitial(): SectionsState {
        val sections = userInterestsProvider.getInterests().map { genre ->
            SectionUiModel(
                id = genre.id,
                numericId = genre.numericId,
                title = genre.name,
            )
        }

        return SectionsState(genreSections = sections)
    }
}
