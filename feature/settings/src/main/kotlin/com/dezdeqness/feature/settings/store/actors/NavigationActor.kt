package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.settings.models.InitialSectionPreference
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.SelectSectionItem
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.shared.presentation.mapper.PersonalRibbonMapper
import javax.inject.Inject

private const val ORDER_SECTION_ID = "order_section"
private const val INITIAL_SECTION_ID = "initial_section"
private const val NAVIGATION_HEADER_ID = "navigation_header"

class NavigationActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val statusesProvider: StatusesProvider,
    private val ribbonMapper: PersonalRibbonMapper,
) : SectionActor {

    override val sectionType: SectionType = SectionType.Navigation

    override suspend fun buildSettings(): List<SettingUiPref> {
        val section = settingsRepository.getPreference(InitialSectionPreference)
        val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }
        val orderedStatuses = (
                settingsRepository
                    .getPreference(StatusesOrderPreference)
                    .ifEmpty {
                        statusesProvider.getStatuses().map { it.groupedId }
                    }
                ).mapNotNull { statuses[it] }
        val isAuthorized = authRepository.isAuthorized()
        val sectionItem = SelectSectionItem.getById(section.id)

        val items = mutableListOf(
            SettingUiPref.HeaderSetting(
                id = NAVIGATION_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_navigation_section
            ),
            SettingUiPref.ActionSetting(
                id = INITIAL_SECTION_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_initial_tab_page,
                subtitleResId = sectionItem.titleId,
            )
        )

        if (isAuthorized) {
            items.add(
                SettingUiPref.ActionSetting(
                    id = ORDER_SECTION_ID,
                    sectionType = sectionType,
                    titleResId = R.string.settings_order_of_statuses,
                    subtitle = orderedStatuses.map(ribbonMapper::map)
                        .joinToString(", ") { it.displayName },
                )
            )
        }

        return items
    }

    override suspend fun handleClick(
        settingId: String,
        currentSetting: SettingUiPref
    ): ActorResult {
        return ActorResult()
    }
}
