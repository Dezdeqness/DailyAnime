package com.dezdeqness.data.core.config

enum class ConfigKeys(
    val key: String,
    val defaultValue: Any,
) {
    BASE_URL(
        key = "base_url",
        defaultValue = "https://shikimori.one/",
    ),
    BASE_SHIKIMORI_GRAPHQL_URL(
        key = "base_graphql_url",
        defaultValue = "https://shikimori.one/api/graphql"
    ),
    BASE_ANILIST_GRAPHQL_URL(
        key = "base_anilist_graphql_url",
        defaultValue = "https://graphql.anilist.co"
    ),
    SELECT_GENRES_COUNTER(
        key = "select_genres_counter",
        defaultValue = 3,
    ),
    CALENDAR_ENABLED(
        key = "calendar_enabled",
        defaultValue = true,
    );

    companion object {
        fun defaults() = entries.associate { it.key to it.defaultValue }
    }
}
