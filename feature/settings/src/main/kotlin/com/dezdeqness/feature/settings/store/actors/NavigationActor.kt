package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.settings.models.InitialSection
import com.dezdeqness.contract.settings.models.InitialSectionPreference
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.SelectSectionItem
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.shared.presentation.mapper.PersonalRibbonMapper
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel
import javax.inject.Inject

private const val ORDER_SECTION_ID = "order_section"
private const val INITIAL_SECTION_ID = "initial_section"
private const val NAVIGATION_HEADER_ID = "navigation_header"

data class InitialSectionSelectPayload(
    val selectedId: Int,
    val items: List<SelectSectionItem>,
) : SettingsNamespace.DialogState.DialogPayload

data class InitialSectionSelectResult(
    val section: InitialSection,
) : SettingsNamespace.DialogState.DialogResult

data class RibbonReorderPayload(
    val statuses: List<RibbonStatusUiModel>,
) : SettingsNamespace.DialogState.DialogPayload

data class RibbonReorderResult(
    val statuses: List<RibbonStatusUiModel>,
) : SettingsNamespace.DialogState.DialogResult

class NavigationActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val statusesProvider: StatusesProvider,
    private val ribbonMapper: PersonalRibbonMapper,
    private val configManager: ConfigManager,
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
        when (settingId) {
            INITIAL_SECTION_ID -> {
                val selected = settingsRepository.getPreference(InitialSectionPreference)
                val items = SelectSectionItem.getSections(
                    isCalendarEnabled = configManager.isCalendarEnabled,
                )

                return ActorResult(
                    dialog = SettingsNamespace.DialogState.ShowModal(
                        payload = InitialSectionSelectPayload(
                            selectedId = selected.id,
                            items = items,
                        ),
                        settingId = settingId,
                    ),
                )
            }

            ORDER_SECTION_ID -> {
                val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }
                val orderedStatuses = (
                        settingsRepository
                            .getPreference(StatusesOrderPreference)
                            .ifEmpty {
                                statusesProvider.getStatuses().map { it.groupedId }
                            }
                        ).mapNotNull { statuses[it] }

                return ActorResult(
                    dialog = SettingsNamespace.DialogState.ShowModal(
                        payload = RibbonReorderPayload(
                            statuses = orderedStatuses.map(ribbonMapper::map),
                        ),
                        settingId = settingId,
                    ),
                )
            }
        }

        return ActorResult()
    }

    override suspend fun saveDialogResult(
        settingId: String,
        data: SettingsNamespace.DialogState.DialogResult,
        currentSetting: SettingUiPref,
    ): ActorResult {
        when (settingId) {
            INITIAL_SECTION_ID -> {
                val result = data as InitialSectionSelectResult
                settingsRepository.setPreference(InitialSectionPreference, result.section)
                return ActorResult(updatedSettings = buildSettings())
            }

            ORDER_SECTION_ID -> {
                val result = data as RibbonReorderResult
                settingsRepository.setPreference(
                    StatusesOrderPreference,
                    result.statuses.map { it.id },
                )
                return ActorResult(updatedSettings = buildSettings())
            }
        }

        return ActorResult()
    }
}
