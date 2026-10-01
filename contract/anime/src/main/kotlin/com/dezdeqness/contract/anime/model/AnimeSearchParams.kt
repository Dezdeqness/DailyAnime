package com.dezdeqness.contract.anime.model

data class AnimeSearchParams(
    val text: String = "",
    val filters: Map<String, Set<String>> = emptyMap(),
)
