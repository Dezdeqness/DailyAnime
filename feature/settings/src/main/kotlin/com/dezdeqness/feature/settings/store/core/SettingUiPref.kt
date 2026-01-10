package com.dezdeqness.feature.settings.store.core

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dezdeqness.feature.settings.store.models.SectionType

sealed class SettingUiPref(
    open val id: String,
    open val sectionType: SectionType,
    open val enabled: Boolean = true,
    @StringRes private val titleResId: Int,
    @StringRes private val subtitleResId: Int? = null,
    private val subtitleArgs: List<Any>? = null,
    private val subtitle: String? = null,
) {

    val uniqueId get() = sectionType.name + id

    @Composable
    fun getTitle(): String {
        return stringResource(titleResId)
    }

    @Composable
    fun getSubtitle(): String? {
        if (!subtitle.isNullOrEmpty()) {
            return subtitle
        }

        val localSubtitleResId = subtitleResId
        if (localSubtitleResId != null) {
            return stringResource(localSubtitleResId, *(subtitleArgs ?: emptyList()).toTypedArray())
        }

        return null
    }

    data class HeaderSetting(
        override val id: String,
        override val sectionType: SectionType,
        @StringRes private val titleResId: Int,
    ) : SettingUiPref(
        id = id,
        sectionType = sectionType,
        titleResId = titleResId,
    )

    data class ActionSetting(
        override val id: String,
        override val sectionType: SectionType,
        override val enabled: Boolean = true,
        val isPostfixShown: Boolean = false,
        @StringRes private val titleResId: Int,
        @StringRes private val subtitleResId: Int? = null,
        private val subtitleArgs: List<Any>? = null,
        private val subtitle: String? = null,
    ) : SettingUiPref(
        id = id,
        sectionType = sectionType,
        enabled = enabled,
        titleResId = titleResId,
        subtitleResId = subtitleResId,
        subtitleArgs = subtitleArgs,
        subtitle = subtitle,
    )

    data class ActionLessSetting(
        override val id: String,
        override val sectionType: SectionType,
        @StringRes private val titleResId: Int,
        @StringRes private val subtitleResId: Int? = null,
        private val subtitleArgs: List<Any>? = null,
        private val subtitle: String? = null,
    ) : SettingUiPref(
        id = id,
        sectionType = sectionType,
        titleResId = titleResId,
        subtitleResId = subtitleResId,
        subtitleArgs = subtitleArgs,
        subtitle = subtitle,
    )

    data class SwitchSetting(
        override val id: String,
        override val sectionType: SectionType,
        override val enabled: Boolean = true,
        val checked: Boolean,
        @StringRes private val titleResId: Int,
        @StringRes private val subtitleResId: Int? = null,
        private val subtitleArgs: List<Any>? = null,
        private val subtitle: String? = null,
    ) : SettingUiPref(
        id = id,
        sectionType = sectionType,
        enabled = enabled,
        titleResId = titleResId,
        subtitleResId = subtitleResId,
        subtitleArgs = subtitleArgs,
        subtitle = subtitle,
    )

    data class ProgressSetting(
        override val id: String,
        override val sectionType: SectionType,
        override val enabled: Boolean = true,
        val progress: Float,
        @StringRes private val titleResId: Int,
        @StringRes private val subtitleResId: Int? = null,
        private val subtitleArgs: List<Any>? = null,
        private val subtitle: String? = null,
    ) : SettingUiPref(
        id = id,
        sectionType = sectionType,
        enabled = enabled,
        titleResId = titleResId,
        subtitleResId = subtitleResId,
        subtitleArgs = subtitleArgs,
        subtitle = subtitle,
    )
}
