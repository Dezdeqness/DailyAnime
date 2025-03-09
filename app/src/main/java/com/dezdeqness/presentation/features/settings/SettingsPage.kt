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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dezdeqness.BuildConfig
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.settings.composables.HeaderSettingsView
import com.dezdeqness.presentation.features.settings.composables.SwitchSettingsView
import com.dezdeqness.presentation.features.settings.composables.TextSettingsView
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    stateFlow: StateFlow<SettingsState>,
    modifier: Modifier = Modifier,
    actions: SettingActions
) {
    val state by stateFlow.collectAsState()

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

    }
}
