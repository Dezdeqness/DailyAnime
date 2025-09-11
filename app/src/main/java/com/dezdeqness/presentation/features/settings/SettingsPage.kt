package com.dezdeqness.presentation.features.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dezdeqness.BuildConfig
import com.dezdeqness.R
import com.dezdeqness.core.ui.TimePickerDialog
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.features.settings.composables.HeaderSettingsView
import com.dezdeqness.presentation.features.settings.composables.SelectRibbonStatusReorderDialog
import com.dezdeqness.presentation.features.settings.composables.SelectSectionDialog
import com.dezdeqness.presentation.features.settings.composables.SelectSectionItem
import com.dezdeqness.presentation.features.settings.composables.SwitchSettingsView
import com.dezdeqness.presentation.features.settings.composables.TextSettingsView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    stateFlow: StateFlow<SettingsState>,
    modifier: Modifier = Modifier,
    actions: SettingActions
) {
    val state by stateFlow.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val listSections = remember(state.isCalendarEnabled) {
        SelectSectionItem.getSections(state.isCalendarEnabled)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                actions.invalidate()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_toolbar_title),
                navigationClick = actions::onBackPressed,
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                HeaderSettingsView(title = stringResource(R.string.settings_navigation_section))
            }

            item {
                TextSettingsView(
                    title = stringResource(R.string.settings_initial_tab_page),
                    subtitle = stringResource(state.selectedSection.titleId),
                    onClick = {
                        actions.onChangeInitialSectionClicked()
                    }
                )
            }

            if (state.isAuthorized) {
                item {
                    TextSettingsView(
                        title = stringResource(R.string.settings_order_of_statuses),
                        subtitle = state.ribbonSettingsSubTitle,
                        onClick = {
                            actions.onChangeRibbonStatusClicked()
                        }
                    )
                }
            }

            item {
                HeaderSettingsView(title = stringResource(R.string.settings_theme_section))
            }

            item {
                SwitchSettingsView(
                    title = stringResource(R.string.settings_dark_theme_title),
                    subtitle = stringResource(id = R.string.settings_dark_theme),
                    checked = state.isDarkThemeEnabled,
                ) { isChecked ->
                    actions.onNightThemeToggleClicked(isChecked)
                }
            }

            item {
                HeaderSettingsView(title = stringResource(R.string.settings_notification_section))
            }

            item {
                SwitchSettingsView(
                    title = stringResource(R.string.settings_notification_title),
                    subtitle = stringResource(R.string.settings_notification_subtitle),
                    checked = state.isNotificationsTurnOn,
                    enabled = state.isNotificationsEnabled,
                ) { isChecked ->
                    actions.onNotificationToggleClicked(isChecked)
                }
            }

            item {
                TextSettingsView(
                    title = stringResource(R.string.settings_notification_time_title),
                    subtitle = state.notificationTimeData.formattedTime,
                    enabled = state.isNotificationsEnabled,
                    onClick = {
                        actions.onNotificationTimePickerClicked()
                    }
                )
            }

            item {
                HeaderSettingsView(title = stringResource(R.string.settings_about_section))
            }
            item {
                TextSettingsView(
                    title = stringResource(R.string.settings_version_title),
                    subtitle = BuildConfig.VERSION_NAME,
                )
            }

            if (BuildConfig.DEBUG) {
                item {
                    TextSettingsView(
                        title = "Debug options",
                        onClick = actions::onDebugOptionsClicked,
                    )
                }
            }
        }

        if (state.isSelectInitialSectionDialogShown) {
            SelectSectionDialog(
                selectedId = state.selectedSection.section.id,
                state = sheetState,
                statuses = listSections,
                onSelectedItem = {
                    actions.onSelectedSectionChanged(it)
                },
                onCloseClicked = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (sheetState.isVisible.not()) {
                            actions.onSelectedSectionDialogClosed()
                        }
                    }
                }
            )
        }

        if (state.isStatusReorderDialogShown) {
            SelectRibbonStatusReorderDialog(
                onDismissRequest = {
                    actions.onChangeRibbonStatusClosed()
                },
                onDoneClicked = {
                    actions.onSelectedRibbonDataChanged(statuses = it)
                    actions.onChangeRibbonStatusClosed()
                },
                statuses = state.personalRibbonStatuses,
            )
        }

        if (state.isNotificationTimePickerDialogShown) {
            TimePickerDialog(
                time = state.notificationTimeData,
                sheetState = sheetState,
                onSaveTime = { hours, minutes ->
                    actions.onNotificationTimeSaved(hours = hours, minutes = minutes)
                },
                onDismissRequest = {
                    actions.onNotificationTimePickerClosed()
                }
            )
        }
    }
}
