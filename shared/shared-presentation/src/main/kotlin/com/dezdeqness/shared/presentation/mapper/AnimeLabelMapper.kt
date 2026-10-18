package com.dezdeqness.shared.presentation.mapper

import com.dezdeqness.contract.anime.model.AgeRating
import com.dezdeqness.contract.anime.model.CharacterRoleKind
import com.dezdeqness.contract.anime.model.RelationKind
import com.dezdeqness.contract.anime.model.VideoEntity
import com.dezdeqness.contract.anime.model.VideoKind
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.shared.presentation.R
import javax.inject.Inject

class AnimeLabelMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun relationTitle(kind: RelationKind): String = resourceProvider.getString(
        when (kind) {
            RelationKind.ADAPTATION -> R.string.anime_relation_adaptation
            RelationKind.PREQUEL -> R.string.anime_relation_prequel
            RelationKind.SEQUEL -> R.string.anime_relation_sequel
            RelationKind.PARENT -> R.string.anime_relation_parent
            RelationKind.SIDE_STORY -> R.string.anime_relation_side_story
            RelationKind.FULL_STORY -> R.string.anime_relation_full_story
            RelationKind.CHARACTER -> R.string.anime_relation_character
            RelationKind.SUMMARY -> R.string.anime_relation_summary
            RelationKind.ALTERNATIVE -> R.string.anime_relation_alternative
            RelationKind.ALTERNATIVE_SETTING -> R.string.anime_relation_alternative_setting
            RelationKind.SPIN_OFF -> R.string.anime_relation_spin_off
            RelationKind.SOURCE -> R.string.anime_relation_source
            RelationKind.COMPILATION -> R.string.anime_relation_compilation
            RelationKind.CONTAINS -> R.string.anime_relation_contains
            RelationKind.SAME_UNIVERSE -> R.string.anime_relation_same_universe
            RelationKind.OTHER -> R.string.anime_relation_other
        },
    )

    fun roleTitle(kind: CharacterRoleKind): String = resourceProvider.getString(
        when (kind) {
            CharacterRoleKind.MAIN -> R.string.anime_character_role_main
            CharacterRoleKind.SUPPORTING -> R.string.anime_character_role_supporting
            CharacterRoleKind.BACKGROUND -> R.string.anime_character_role_background
        },
    )

    fun videoTitle(video: VideoEntity): String = video.name ?: resourceProvider.getString(
        when (video.kind) {
            VideoKind.TRAILER -> R.string.anime_video_trailer
            VideoKind.CHARACTER_TRAILER -> R.string.anime_video_character_trailer
            VideoKind.COMMERCIAL -> R.string.anime_video_commercial
            VideoKind.OPENING -> R.string.anime_video_opening
            VideoKind.ENDING -> R.string.anime_video_ending
            VideoKind.MUSIC -> R.string.anime_video_music
            VideoKind.CLIP -> R.string.anime_video_clip
            VideoKind.EPISODE_PREVIEW -> R.string.anime_video_episode_preview
            VideoKind.OTHER -> R.string.anime_video_other
        },
    )

    fun ageRatingTitle(rating: AgeRating): String? {
        val resId = when (rating) {
            AgeRating.G -> R.string.anime_age_rating_g
            AgeRating.PG -> R.string.anime_age_rating_pg
            AgeRating.PG_13 -> R.string.anime_age_rating_pg_13
            AgeRating.R -> R.string.anime_age_rating_r
            AgeRating.R_PLUS -> R.string.anime_age_rating_r_plus
            AgeRating.RX -> R.string.anime_age_rating_rx
            AgeRating.UNKNOWN -> return null
        }
        return resourceProvider.getString(resId)
    }
}
