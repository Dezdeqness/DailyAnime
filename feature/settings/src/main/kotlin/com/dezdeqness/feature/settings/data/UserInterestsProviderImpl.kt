package com.dezdeqness.feature.settings.data

import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.TypeEntity
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.settings.repository.UserInterestsProvider
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.ConfigurationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInterestsProviderImpl(
    private val configurationProvider: ConfigurationProvider,
    private val configManager: ConfigManager,
    private val settingsRepository: SettingsRepository,
) : UserInterestsProvider {

    override suspend fun getInterests(): List<GenreEntity> {
        val userSelected = settingsRepository.getPreference(UserSelectedInterestsPreference)
        val ids = userSelected.ifEmpty { configManager.homeGenresList }
        return resolveInterests(ids)
    }

    override suspend fun getInterestIds(): List<String> = getInterests().map { it.numericId }

    override fun observeInterests(): Flow<List<GenreEntity>> =
        settingsRepository.observePreference(UserSelectedInterestsPreference)
            .map { userSelected ->
                val ids = userSelected.ifEmpty { configManager.homeGenresList }
                resolveInterests(ids)
            }

    private fun resolveInterests(ids: List<String>): List<GenreEntity> =
        configurationProvider
            .getListGenre()
            .filter { it.type == TypeEntity.ANIME }
            .filter { ids.contains(it.numericId) }
}
