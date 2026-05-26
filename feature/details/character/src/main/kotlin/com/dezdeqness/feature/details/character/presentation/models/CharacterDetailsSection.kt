package com.dezdeqness.feature.details.character.presentation.models

import com.dezdeqness.feature.details.common.presentation.DetailsSection

sealed interface CharacterDetailsSection : DetailsSection {
    data class Seyu(val items: List<SeyuItem>) : CharacterDetailsSection {
        override val rendererType: String = TYPE
        companion object {
            const val TYPE = "character_seyu"
        }
    }

    data class Animes(val items: List<AnimeItem>) : CharacterDetailsSection {
        override val rendererType: String = TYPE
        companion object {
            const val TYPE = "character_animes"
        }
    }
}

data class SeyuItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
)

data class AnimeItem(
    val id: Long,
    val title: String,
    val kind: String,
    val imageUrl: String,
)
