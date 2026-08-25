package com.dezdeqness.feature.achievements.data

import android.content.res.AssetManager
import com.dezdeqness.contract.achievements.model.AchievementConfigDataEntity
import com.dezdeqness.contract.achievements.model.AchievementConfigEntity
import com.dezdeqness.contract.achievements.repository.AchievementConfigRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import okio.buffer
import okio.source

internal class AchievementConfigRepositoryImpl @Inject constructor(
    private val assetManager: AssetManager,
    private val achievementMapper: AchievementMapper,
    private val moshi: Moshi,
) : AchievementConfigRepository {

    override fun getConfig(): AchievementConfigDataEntity {
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
        tag: String,
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
        private const val FILENAME_ACHIEVEMENTS_JSON = "achievements_config.json"
        private const val ACHIEVEMENTS_TAG_COMMON = "common"
        private const val ACHIEVEMENTS_TAG_GENRES = "genres"
    }
}
