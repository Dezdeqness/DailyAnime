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
    ),
    HOME_GENRES_LIST_IDS(
        key = "home_genres_list_ids",
        defaultValue = "2,27,22", // GENRE_ID_ADVENTURE, GENRE_ID_ROMANTIC, GENRE_ID_SHOUNEN
    );

    companion object {
        fun defaults() = entries.associate { it.key to it.defaultValue }
        fun getByKey(key: String) = entries.first { it.key == key }
    }
}
