package com.dezdeqness.presentation.features.details

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@Serializable
data class AnimeStatsTransferModel(
    val name: String,
    val value: Int,
)


val json = Json { encodeDefaults = true }

fun serializeListToString(list: List<AnimeStatsTransferModel>): String {
    val raw = json.encodeToString(ListSerializer(AnimeStatsTransferModel.serializer()), list)
    return URLEncoder.encode(raw, Charsets.UTF_8.name())
}

fun deserializeListFromString(encoded: String): List<AnimeStatsTransferModel> {
    val decoded = URLDecoder.decode(encoded, Charsets.UTF_8.name())
    return json.decodeFromString(ListSerializer(AnimeStatsTransferModel.serializer()), decoded)
}
