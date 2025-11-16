package com.dezdeqness.data.provider

import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.TypeEntity
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.config.ConfigManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeGenresProvider @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val configManager: ConfigManager,
    private val settingsRepository: SettingsRepository,
) {

    fun getHomeSectionGenresIds() =
        getHomeSectionGenres().map { it.numericId }

    fun getHomeSectionGenres(): List<GenreEntity> {
        val userSelected = runBlocking {
            settingsRepository.getPreference(UserSelectedInterestsPreference)
        }

        val ids = userSelected.ifEmpty {
            configManager.homeGenresList
        }

        return configurationProvider
            .getListGenre()
            .filter { it.type == TypeEntity.ANIME }
            .filter { ids.contains(it.numericId) }
    }

}
