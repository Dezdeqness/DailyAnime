package com.dezdeqness.feature.settings.store.actors

import android.content.Context
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.dezdeqness.contract.settings.models.ImageCacheMaxSizePreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.feature.settings.utils.formatSize
import javax.inject.Inject

private const val CLEAR_CACHE_ID = "clear_cache"
private const val MAX_CACHE_SIZE_ID = "max_cache_size"
private const val IMAGE_CACHE_PROGRESS_ID = "image_cache_progress"
private const val STORAGE_HEADER_ID = "storage_header"

class StorageActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val context: Context,
) : SectionActor {

    override val sectionType: SectionType = SectionType.Storage

    @OptIn(ExperimentalCoilApi::class)
    override suspend fun buildSettings(): List<SettingUiPref> {
        val maxSize = settingsRepository.getPreference(ImageCacheMaxSizePreference)
        val imageDiskCache = context.imageLoader.diskCache
        val imageCacheSize = imageDiskCache?.size ?: 0
        val imageCacheMaxSize = imageDiskCache?.maxSize ?: 1
        val imageCacheProgress = (imageCacheSize.toFloat() / imageCacheMaxSize).coerceIn(
            0f,
            1f
        )

        return listOf(
            SettingUiPref.HeaderSetting(
                id = STORAGE_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_storage_title
            ),
            SettingUiPref.ProgressSetting(
                id = IMAGE_CACHE_PROGRESS_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_image_cache_title,
                subtitleResId = R.string.settings_image_cache_used_title,
                subtitleArgs = listOf(
                    formatSize(imageCacheSize),
                    formatSize(imageCacheMaxSize),
                ),
                progress = imageCacheProgress
            ),
            SettingUiPref.ActionSetting(
                id = MAX_CACHE_SIZE_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_max_image_cache_title,
                subtitle = formatSize(maxSize * 1024 * 1024L),
            ),
            SettingUiPref.ActionSetting(
                id = CLEAR_CACHE_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_image_cache_clear_title,
            )
        )
    }

    override suspend fun handleClick(
        settingId: String,
        currentSetting: SettingUiPref
    ): ActorResult {
        return ActorResult()
    }
}
