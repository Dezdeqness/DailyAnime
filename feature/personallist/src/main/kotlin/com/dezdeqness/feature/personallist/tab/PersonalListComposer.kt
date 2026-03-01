package com.dezdeqness.feature.personallist.tab

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.user.model.FullAnimeStatusesEntity
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.shared.presentation.mapper.PersonalRibbonMapper
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject

class PersonalListComposer @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
    private val ribbonMapper: PersonalRibbonMapper,
    private val settingsRepository: SettingsRepository,
    private val statusesProvider: StatusesProvider,
) {

    fun compose(
        entityList: List<UserRateEntity>,
    ) = entityList.mapNotNull(::convert)

    suspend fun composeStatuses(
        fullAnimeStatusesEntity: FullAnimeStatusesEntity,
    ): List<RibbonStatusUiModel> {
        val list = fullAnimeStatusesEntity
            .list
            .filter { item -> item.size != 0L }
            .associateBy { it.groupedId }

        return settingsRepository
            .getPreference(StatusesOrderPreference)
            .ifEmpty { statusesProvider.getStatuses().map { it.groupedId } }
            .mapNotNull { list[it] }
            .map(ribbonMapper::map)
    }

    private fun convert(item: UserRateEntity): UserRateUiModel? {

        if (item.anime == null) {
            return null
        }

        val anime = item.anime ?: return null

        val score = if (item.score == 0L) {
            "-"
        } else {
            item.score.toString()
        }

        val kind = animeKindUtils.mapKind(anime.kind)

        return UserRateUiModel(
            rateId = item.id,
            id = anime.id,
            name = anime.russian,
            score = score,
            kind = kind,
            episodes = item.episodes.toInt(),
            logoUrl = anime.image.original,
            overallEpisodes = anime.episodes,
            updatedAtTimestamp = item.updatedAtTimestamp,
        )
    }

    fun convertOnlyUserAnime(item: UserRateEntity): UserRateUiModel? {
        val score = if (item.score == 0L) {
            "-"
        } else {
            item.score.toString()
        }

        return UserRateUiModel(
            rateId = item.id,
            id = -1,
            name = "",
            score = score,
            kind = "",
            episodes = item.episodes.toInt(),
            logoUrl = "",
            overallEpisodes = -1,
            updatedAtTimestamp = item.updatedAtTimestamp,
        )
    }

}
