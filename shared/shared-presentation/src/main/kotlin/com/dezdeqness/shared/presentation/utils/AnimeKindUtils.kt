package com.dezdeqness.shared.presentation.utils

import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.core.provider.ResourceProvider
import com.dezdeqness.shared.presentation.R
import javax.inject.Inject

class AnimeKindUtils @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun mapKind(kind: AnimeKind) =
        when (kind) {
            AnimeKind.TV -> resourceProvider.getString(R.string.anime_kind_tv)
            AnimeKind.MOVIE -> resourceProvider.getString(R.string.anime_kind_film)
            AnimeKind.OVA -> resourceProvider.getString(R.string.anime_kind_ova)
            AnimeKind.ONA -> resourceProvider.getString(R.string.anime_kind_ona)
            AnimeKind.SPECIAL -> resourceProvider.getString(R.string.anime_kind_special)
            AnimeKind.TV_SPECIAL -> resourceProvider.getString(R.string.anime_kind_tv_special)
            AnimeKind.MUSIC -> resourceProvider.getString(R.string.anime_kind_music)
            AnimeKind.PROMO -> resourceProvider.getString(R.string.anime_kind_promo)
            AnimeKind.AD -> resourceProvider.getString(R.string.anime_kind_ad)
            else -> ""
        }

}
