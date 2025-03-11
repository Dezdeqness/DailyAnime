package com.dezdeqness.presentation.features.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dezdeqness.BuildConfig
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
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

    val listSections = remember {
        SelectSectionItem.getSections()
    }

    Scaffold(
        containerColor = colorResource(id = R.color.background_tint),
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings_toolbar_title),
                        color = AppTheme.colors.textPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { actions.onBackPressed() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = AppTheme.colors.onSecondary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = AppTheme.colors.onPrimary,
                ),
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
                    onSettingClick = {
                        actions.onChangeInitialSectionClicked()
                    }
                )
            }

            if (state.isAuthorized) {
                item {
                    TextSettingsView(
                        title = stringResource(R.string.settings_order_of_statuses),
                        subtitle = state.ribbonSettingsSubTitle,
                        onSettingClick = {
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
                HeaderSettingsView(title = stringResource(R.string.settings_about_section))
            }
            item {
                TextSettingsView(
                    title = stringResource(R.string.settings_version_title),
                    subtitle = BuildConfig.VERSION_NAME,
                )
            }
        }

        if (state.isSelectInitialSectionDialogShown) {
            SelectSectionDialog(
                selectedId = state.selectedSection.id,
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


    }
}
