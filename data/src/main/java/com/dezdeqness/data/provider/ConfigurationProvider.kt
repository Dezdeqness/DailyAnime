package com.dezdeqness.data.provider

import android.content.res.AssetManager
import com.dezdeqness.data.mapper.AchievementMapper
import com.dezdeqness.data.mapper.FilterMapper
import com.dezdeqness.data.mapper.GenreMapper
import com.dezdeqness.data.model.AchievementConfigRemote
import com.dezdeqness.data.model.FilterItem
import com.dezdeqness.data.model.GenreRemote
import com.dezdeqness.domain.model.AchievementConfigDataEntity
import com.dezdeqness.domain.model.AchievementConfigEntity
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.GenreEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.buffer
import okio.source

class ConfigurationProvider(
    private val assetManager: AssetManager,
    private val genreMapper: GenreMapper,
    private val filterMapper: FilterMapper,
    private val achievementMapper: AchievementMapper,
    private val moshi: Moshi,
) {

    fun getListGenre(): List<GenreEntity> {
        val inputStream = assetManager.open(FILENAME_GENRE_JSON)
        val type = Types.newParameterizedType(List::class.java, GenreRemote::class.java)
        val adapter = moshi.adapter<List<GenreRemote>>(type)
        val list = adapter.fromJson(inputStream.source().buffer()).orEmpty()
        return list.map { item -> genreMapper.fromResponse(item) }
    }

    fun getFilters(): List<FilterEntity> {
        val inputStream = assetManager.open(FILENAME_FILTER_JSON)
        val type = Types.newParameterizedType(List::class.java, FilterItem::class.java)
        val adapter = moshi.adapter<List<FilterItem>>(type)
        val list = adapter.fromJson(inputStream.source().buffer()).orEmpty()
        return list.map { item -> filterMapper.fromResponse(item) }
    }

    fun getAchievementConfig(): AchievementConfigDataEntity {
        val inputStream = assetManager.open(FILENAME_ACHIEVEMENTS_JSON)

        val rootType =
            Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
        val rootAdapter = moshi.adapter<Map<String, Any>>(rootType)
        val rootJson = rootAdapter.fromJson(inputStream.source().buffer())
            ?: return AchievementConfigDataEntity()

        val common = parseAchievementByTag(rootJson, ACHIEVEMENTS_TAG_COMMON)
        val genres = parseAchievementByTag(rootJson, ACHIEVEMENTS_TAG_GENRES)

        return AchievementConfigDataEntity(
            common = common,
            genres = genres,
        )
    }

    private fun parseAchievementByTag(
        rootJson: Map<String, Any>,
        tag: String
    ): Map<String, List<AchievementConfigEntity>> {
        val commonRaw = rootJson[tag] ?: return emptyMap()
        val commonJson = moshi.adapter(Any::class.java).toJson(commonRaw)

        val listType =
            Types.newParameterizedType(List::class.java, AchievementConfigRemote::class.java)
        val mapType = Types.newParameterizedType(Map::class.java, String::class.java, listType)
        val commonAdapter = moshi.adapter<Map<String, List<AchievementConfigRemote>>>(mapType)
        val parsed = commonAdapter.fromJson(commonJson).orEmpty()
        return parsed.mapValues { it.value.map(achievementMapper::fromResponse) }
    }

    companion object {
        private const val FILENAME_GENRE_JSON = "genre.json"
        private const val FILENAME_FILTER_JSON = "filter.json"
        private const val FILENAME_ACHIEVEMENTS_JSON = "achievements_config.json"

        private const val ACHIEVEMENTS_TAG_COMMON = "common"
        private const val ACHIEVEMENTS_TAG_GENRES = "genres"
    }

}
