package com.dezdeqness.utils

import com.dezdeqness.R
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.data.provider.ResourceProvider
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
