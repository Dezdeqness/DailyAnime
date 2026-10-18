package com.dezdeqness.data.anilist.anime.mapper

import com.dezdeqness.contract.anime.model.AgeRating
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.CharacterEntity
import com.dezdeqness.contract.anime.model.CharacterRoleKind
import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.GenreKindEntity
import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RelationKind
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.contract.anime.model.ScoreStat
import com.dezdeqness.contract.anime.model.StatusStat
import com.dezdeqness.contract.anime.model.StudioEntity
import com.dezdeqness.contract.anime.model.TypeEntity
import com.dezdeqness.contract.anime.model.UserRateStatus
import com.dezdeqness.contract.anime.model.VideoEntity
import com.dezdeqness.contract.anime.model.VideoKind
import com.dezdeqness.data.anilist.graphql.AnimeDetailsQuery
import com.dezdeqness.data.anilist.graphql.fragment.AnimeCharacter
import com.dezdeqness.data.anilist.graphql.fragment.AnimeRelation
import com.dezdeqness.data.anilist.graphql.type.CharacterRole
import com.dezdeqness.data.anilist.graphql.type.MediaListStatus
import com.dezdeqness.data.anilist.graphql.type.MediaRelation
import com.dezdeqness.data.anilist.graphql.type.MediaType
import javax.inject.Inject

class AnilistAnimeDetailsMapper @Inject constructor(
    private val briefMapper: AnilistAnimeMapper,
) {

    fun mapDetails(media: AnimeDetailsQuery.Media): AnimeDetailsEntity {
        val brief = briefMapper.map(media.animeBrief)
        val title = media.animeBrief.title

        return AnimeDetailsEntity(
            id = brief.id,
            name = brief.name,
            russian = brief.russian,
            english = title?.english.orEmpty(),
            japanese = title?.native.orEmpty(),
            score = brief.score,
            kind = brief.kind,
            url = brief.url,
            duration = media.duration?.toString().orEmpty(),
            rating = AgeRating.UNKNOWN,
            status = brief.status,
            airedOnTimestamp = brief.airedOnTimestamp,
            releasedOnTimestamp = brief.releasedOnTimestamp,
            episodes = brief.episodes,
            episodesAired = brief.episodesAired,
            image = brief.image,
            description = media.description,
            descriptionHTML = media.description.orEmpty(),
            studioList = media.studios?.nodes.orEmpty().filterNotNull().map(::mapStudio),
            genreList = media.genres.orEmpty().filterNotNull().map(::mapGenre),
            videoList = listOfNotNull(media.trailer?.let(::mapTrailer)),
            nextEpisodeAtTimestamp = brief.nextEpisodeTimestamp,
            userRate = null,
            scoresStats = media.stats?.scoreDistribution.orEmpty()
                .filterNotNull()
                .mapNotNull(::mapScore)
                .sortedByDescending { it.score },
            statusesStats = media.stats?.statusDistribution.orEmpty()
                .filterNotNull()
                .mapNotNull(::mapStatus),
        )
    }

    fun mapRelation(relation: AnimeRelation): RelatedItemEntity? {
        val node = relation.node ?: return null
        if (node.type != MediaType.ANIME) return null

        return RelatedItemEntity(
            animeBriefEntity = briefMapper.map(node.animeBrief),
            kind = relationKinds[relation.relationType] ?: RelationKind.OTHER,
        )
    }

    fun mapRole(character: AnimeCharacter): RoleEntity? {
        val node = character.node ?: return null

        return RoleEntity(
            roles = listOfNotNull(roleKinds[character.role]),
            character = CharacterEntity(
                id = node.id.toLong(),
                name = node.name?.let { it.userPreferred ?: it.full }.orEmpty(),
                russian = "",
                image = ImageEntity(
                    original = node.image?.let { it.large ?: it.medium }.orEmpty(),
                    preview = node.image?.let { it.medium ?: it.large }.orEmpty(),
                ),
                url = node.siteUrl.orEmpty(),
            ),
        )
    }

    private fun mapStudio(studio: AnimeDetailsQuery.Node) = StudioEntity(
        id = studio.id,
        name = studio.name,
        filteredName = studio.name,
        real = true,
    )

    private fun mapGenre(genre: String) = GenreEntity(
        numericId = genre,
        id = genre,
        name = genre,
        type = TypeEntity.ANIME,
        kind = GenreKindEntity.GENRE,
    )

    private fun mapTrailer(trailer: AnimeDetailsQuery.Trailer): VideoEntity? {
        val id = trailer.id ?: return null
        val url = when (trailer.site) {
            YOUTUBE -> "https://www.youtube.com/watch?v=$id"
            DAILYMOTION -> "https://www.dailymotion.com/video/$id"
            else -> return null
        }

        return VideoEntity(
            id = 0,
            name = null,
            playerUrl = url,
            url = url,
            imageUrl = trailer.thumbnail.orEmpty(),
            kind = VideoKind.TRAILER,
            hosting = trailer.site.orEmpty(),
        )
    }

    private fun mapScore(score: AnimeDetailsQuery.ScoreDistribution): ScoreStat? {
        val value = (score.score ?: return null) / SCORE_SCALE
        if (value !in MIN_SCORE..MAX_SCORE) return null
        return ScoreStat(score = value, count = score.amount ?: 0)
    }

    private fun mapStatus(status: AnimeDetailsQuery.StatusDistribution): StatusStat? {
        val userRateStatus = userRateStatuses[status.status] ?: return null
        return StatusStat(status = userRateStatus, count = status.amount ?: 0)
    }

    private companion object {
        const val YOUTUBE = "youtube"
        const val DAILYMOTION = "dailymotion"
        const val SCORE_SCALE = 10
        const val MIN_SCORE = 1
        const val MAX_SCORE = 10

        private val relationKinds = mapOf(
            MediaRelation.ADAPTATION to RelationKind.ADAPTATION,
            MediaRelation.PREQUEL to RelationKind.PREQUEL,
            MediaRelation.SEQUEL to RelationKind.SEQUEL,
            MediaRelation.PARENT to RelationKind.PARENT,
            MediaRelation.SIDE_STORY to RelationKind.SIDE_STORY,
            MediaRelation.CHARACTER to RelationKind.CHARACTER,
            MediaRelation.SUMMARY to RelationKind.SUMMARY,
            MediaRelation.ALTERNATIVE to RelationKind.ALTERNATIVE,
            MediaRelation.SPIN_OFF to RelationKind.SPIN_OFF,
            MediaRelation.SOURCE to RelationKind.SOURCE,
            MediaRelation.COMPILATION to RelationKind.COMPILATION,
            MediaRelation.CONTAINS to RelationKind.CONTAINS,
            MediaRelation.SAME_UNIVERSE to RelationKind.SAME_UNIVERSE,
        )

        private val roleKinds = mapOf(
            CharacterRole.MAIN to CharacterRoleKind.MAIN,
            CharacterRole.SUPPORTING to CharacterRoleKind.SUPPORTING,
            CharacterRole.BACKGROUND to CharacterRoleKind.BACKGROUND,
        )

        private val userRateStatuses = mapOf(
            MediaListStatus.CURRENT to UserRateStatus.WATCHING,
            MediaListStatus.PLANNING to UserRateStatus.PLANNED,
            MediaListStatus.COMPLETED to UserRateStatus.COMPLETED,
            MediaListStatus.DROPPED to UserRateStatus.DROPPED,
            MediaListStatus.PAUSED to UserRateStatus.ON_HOLD,
            MediaListStatus.REPEATING to UserRateStatus.REWATCHING,
        )
    }
}
