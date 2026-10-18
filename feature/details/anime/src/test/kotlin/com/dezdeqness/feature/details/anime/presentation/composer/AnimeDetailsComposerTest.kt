package com.dezdeqness.feature.details.anime.presentation.composer

import com.dezdeqness.contract.anime.model.AgeRating
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.CharacterEntity
import com.dezdeqness.contract.anime.model.CharacterRoleKind
import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RelationKind
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.contract.anime.model.VideoEntity
import com.dezdeqness.contract.anime.model.VideoKind
import com.dezdeqness.contract.source.SourceAnimeStats
import com.dezdeqness.contract.source.SourceCharacterDetails
import com.dezdeqness.contract.source.SourceChronology
import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.contract.source.SourceFeature
import com.dezdeqness.contract.source.SourceSimilar
import com.dezdeqness.contract.source.SourceType
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry
import com.dezdeqness.shared.presentation.mapper.AnimeLabelMapper
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import com.dezdeqness.shared.presentation.utils.UrlNormalizer
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class AnimeDetailsComposerTest {

    private val resourceProvider = mockk<ResourceProvider>(relaxed = true)
    private val animeKindUtils = mockk<AnimeKindUtils>(relaxed = true)
    private val animeLabelMapper = mockk<AnimeLabelMapper>(relaxed = true)
    private val urlNormalizer = mockk<UrlNormalizer> {
        every { normalize(any()) } answers { firstArg() }
    }

    @Test
    fun `WHEN russian is empty SHOULD use name as title`() {
        val sections = composer(features = emptySet()).compose(details(russian = ""))

        assertEquals(TitleSection(text = "Berserk"), sections.filterIsInstance<TitleSection>().single())
    }

    @Test
    fun `WHEN source has no more info features SHOULD skip MoreInfo section`() {
        val sections = composer(features = emptySet()).compose(details())

        assertTrue(sections.none { it is AnimeDetailsSection.MoreInfo })
    }

    @Test
    fun `WHEN source supports only stats SHOULD show only stats in MoreInfo`() {
        val sections = composer(features = setOf(Stats)).compose(details())

        assertEquals(
            AnimeDetailsSection.MoreInfo(
                isSimilarVisible = false,
                isChronologyVisible = false,
                isStatsVisible = true,
            ),
            sections.filterIsInstance<AnimeDetailsSection.MoreInfo>().single(),
        )
    }

    @Test
    fun `WHEN source supports all more info features SHOULD show all of them`() {
        val sections = composer(features = setOf(Stats, Similar, Chronology)).compose(details())

        assertEquals(
            AnimeDetailsSection.MoreInfo(
                isSimilarVisible = true,
                isChronologyVisible = true,
                isStatsVisible = true,
            ),
            sections.filterIsInstance<AnimeDetailsSection.MoreInfo>().single(),
        )
    }

    @Test
    fun `WHEN source has no character details SHOULD make characters not clickable`() {
        val sections = composer(features = emptySet()).compose(details())

        assertEquals(false, sections.filterIsInstance<AnimeDetailsSection.Characters>().single().isClickable)
    }

    @Test
    fun `WHEN source has character details SHOULD make characters clickable`() {
        val sections = composer(features = setOf(CharacterDetails)).compose(details())

        assertEquals(true, sections.filterIsInstance<AnimeDetailsSection.Characters>().single().isClickable)
    }

    @Test
    fun `WHEN relation is present SHOULD take its title from label mapper`() {
        every { animeLabelMapper.relationTitle(RelationKind.SEQUEL) } returns "Sequel"
        val related = RelatedItemEntity(animeBriefEntity = brief(), kind = RelationKind.SEQUEL)

        val sections = composer(features = emptySet()).compose(details(relates = listOf(related)))

        assertEquals("Sequel", sections.filterIsInstance<AnimeDetailsSection.Related>().single().items.single().type)
    }

    @Test
    fun `WHEN video is present SHOULD take its name from label mapper`() {
        val video = VideoEntity(
            id = 1,
            name = null,
            playerUrl = "",
            url = "https://youtu.be/1",
            imageUrl = "",
            kind = VideoKind.TRAILER,
            hosting = "youtube",
        )
        every { animeLabelMapper.videoTitle(video) } returns "Trailer"

        val sections = composer(features = emptySet()).compose(details(videos = listOf(video)))

        assertEquals("Trailer", sections.filterIsInstance<AnimeDetailsSection.Videos>().single().items.single().name)
    }

    @Test
    fun `WHEN age rating is unknown SHOULD skip age rating entry`() {
        every { resourceProvider.getString(R.string.anime_details_brief_age_rating) } returns AGE_RATING
        every { animeLabelMapper.ageRatingTitle(AgeRating.UNKNOWN) } returns null

        val sections = composer(features = emptySet()).compose(details())

        val entries = sections.filterIsInstance<BriefInfoSection>().single().items
        assertTrue(entries.none { it.title == AGE_RATING })
    }

    @Test
    fun `WHEN age rating is known SHOULD add age rating entry`() {
        every { resourceProvider.getString(R.string.anime_details_brief_age_rating) } returns AGE_RATING
        every { animeLabelMapper.ageRatingTitle(AgeRating.R) } returns "17+"

        val sections = composer(features = emptySet()).compose(details(rating = AgeRating.R))

        val entries = sections.filterIsInstance<BriefInfoSection>().single().items
        assertTrue(entries.contains(BriefInfoEntry(title = AGE_RATING, info = "17+")))
    }

    private fun composer(features: Set<SourceFeature>) = AnimeDetailsComposer(
        resourceProvider = resourceProvider,
        animeKindUtils = animeKindUtils,
        urlNormalizer = urlNormalizer,
        sourceConfig = object : SourceConfig {
            override val type = SourceType.ANILIST
            override val features = features
        },
        animeLabelMapper = animeLabelMapper,
    )

    private fun brief() = AnimeBriefEntity(
        id = 34L,
        name = "Berserk: Sequel",
        russian = "",
        image = ImageEntity(),
        url = "",
        kind = AnimeKind.TV,
        score = 0f,
        status = AnimeStatus.RELEASED,
        episodes = 0,
        episodesAired = 0,
        airedOnTimestamp = 0L,
        releasedOnTimestamp = 0L,
    )

    private fun details(
        russian: String = "Берсерк",
        rating: AgeRating = AgeRating.UNKNOWN,
        relates: List<RelatedItemEntity> = emptyList(),
        videos: List<VideoEntity> = emptyList(),
    ) = AnimeDetailsFullEntity(
        animeDetailsEntity = AnimeDetailsEntity(
            id = 33L,
            name = "Berserk",
            russian = russian,
            english = "",
            japanese = "",
            score = 8.5f,
            kind = AnimeKind.TV,
            url = "",
            duration = "24",
            rating = rating,
            status = AnimeStatus.RELEASED,
            airedOnTimestamp = 1_000_000_000_000L,
            releasedOnTimestamp = 1_010_000_000_000L,
            episodes = 25,
            episodesAired = 25,
            image = ImageEntity(),
            description = null,
            descriptionHTML = "",
            studioList = emptyList(),
            genreList = emptyList(),
            videoList = videos,
            nextEpisodeAtTimestamp = 0L,
            userRate = null,
            scoresStats = emptyList(),
            statusesStats = emptyList(),
        ),
        screenshots = emptyList(),
        relates = relates,
        roles = listOf(
            RoleEntity(
                roles = listOf(CharacterRoleKind.MAIN),
                character = CharacterEntity(id = 1L, name = "Guts", russian = "", image = ImageEntity(), url = ""),
            ),
        ),
    )

    private companion object {
        const val AGE_RATING = "Age rating"
    }

    private object Stats : SourceAnimeStats

    private object Similar : SourceSimilar

    private object Chronology : SourceChronology

    private object CharacterDetails : SourceCharacterDetails
}
