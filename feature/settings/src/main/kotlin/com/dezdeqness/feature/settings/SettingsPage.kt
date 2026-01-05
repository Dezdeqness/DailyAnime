package com.dezdeqness.feature.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.dezdeqness.contract.settings.models.ThemeMode
import com.dezdeqness.core.BuildConfig
import com.dezdeqness.core.ui.dialogs.TimePickerDialog
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.feature.settings.composables.ListPreferencesDialog
import com.dezdeqness.feature.settings.composables.ProgressSettingsView
import com.dezdeqness.feature.settings.composables.SettingsSection
import com.dezdeqness.feature.settings.composables.SwitchSettingsView
import com.dezdeqness.feature.settings.composables.TextSettingsView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun SettingsPage(
    stateFlow: StateFlow<SettingsState>,
    modifier: Modifier = Modifier,
    actions: SettingActions
) {
    val context = LocalContext.current

    val state by stateFlow.collectAsState()

    val imageDiskCache = context.imageLoader.diskCache ?: return

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
                navigationIcon = Icons.Filled.Close,
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_theme_section),
                    content = {
                        TextSettingsView(
                            title = stringResource(R.string.settings_dark_theme_title),
                            subtitle = stringResource(state.theme.mode.fromMode()),
                            onClick = { actions.onThemeClicked() },
                        )
                    }
                )
            }

            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_notification_section),
                    content = {
                        SwitchSettingsView(
                            title = stringResource(R.string.settings_notification_title),
                            subtitle = stringResource(R.string.settings_notification_subtitle),
                            checked = state.notifications.isTurnedOn,
                            enabled = state.notifications.isEnabled,
                        ) { isChecked ->
                            actions.onNotificationToggleClicked(isChecked)
                        }

                        TextSettingsView(
                            title = stringResource(R.string.settings_notification_time_title),
                            subtitle = state.notifications.time.formattedTime,
                            enabled = state.notifications.isEnabled,
                            onClick = {
                                actions.onNotificationTimePickerClicked()
                            }
                        )
                    }
                )
            }

            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_content_section),
                    content = {
                        TextSettingsView(
                            title = stringResource(R.string.settings_select_interests_title),
                            subtitle = state.content.selectedInterestsSubtitle,
                            suffixIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = AppTheme.colors.textPrimary,
                                )
                            },
                            onClick = {
                                actions.onSelectInterestsClicked()
                            }
                        )

                        SwitchSettingsView(
                            title = stringResource(R.string.settings_adult_content_title),
                            checked = !state.content.isAdultEnabled,
                        ) { isChecked ->
                            actions.onChangeAdultContentClicked(!isChecked)
                        }
                    }
                )
            }

            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_navigation_section),
                    content = {
                        TextSettingsView(
                            title = stringResource(R.string.settings_initial_tab_page),
                            subtitle = stringResource(SelectSectionItem.getById(state.navigation.selectedSection.id).titleId),
                            onClick = {
                                actions.onChangeInitialSectionClicked()
                            }
                        )

                        if (state.isAuthorized) {
                            TextSettingsView(
                                title = stringResource(R.string.settings_order_of_statuses),
                                subtitle = state.ribbon.subtitle,
                                onClick = {
                                    actions.onChangeRibbonStatusClicked()
                                }
                            )
                        }
                    }
                )
            }

            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_storage_title),
                    content = {
                        var imageCacheSize by remember(state.cache.maxImageSize) {
                            mutableLongStateOf(imageDiskCache.size)
                        }

                        LaunchedEffect(imageDiskCache) {
                            while (isActive) {
                                delay(500)
                                imageCacheSize = imageDiskCache.size
                            }
                        }

                        val imageCacheProgress by animateFloatAsState(
                            targetValue = (imageCacheSize.toFloat() / imageDiskCache.maxSize).coerceIn(
                                0f,
                                1f
                            ),
                            label = ""
                        )
                        ProgressSettingsView(
                            title = stringResource(R.string.settings_image_cache_title),
                            subtitle = stringResource(
                                R.string.settings_image_cache_used_title,
                                formatSize(imageCacheSize),
                                formatSize(imageDiskCache.maxSize)
                            ),
                            progress = imageCacheProgress,
                        )

                        TextSettingsView(
                            title = stringResource(R.string.settings_max_image_cache_title),
                            subtitle = formatSize(imageDiskCache.maxSize),
                            onClick = {
                                actions.onMaxImageCacheSizeClicked()
                            }
                        )

                        TextSettingsView(
                            title = stringResource(R.string.settings_image_cache_clear_title),
                            onClick = {
                                imageDiskCache.clear()
                            }
                        )
                    }
                )
            }

            item {
                SettingsSection(
                    sectionTitle = stringResource(R.string.settings_about_section),
                    content = {
                        TextSettingsView(
                            title = stringResource(R.string.settings_version_title),
                            subtitle = BuildConfig.VERSION_NAME,
                        )

                        if (BuildConfig.DEBUG) {
                            TextSettingsView(
                                title = "Debug options",
                                onClick = actions::onDebugOptionsClicked,
                            )
                        }
                    }
                )
            }
        }

        if (state.navigation.isDialogShown) {
            SelectSectionDialog(
                selectedId = state.navigation.selectedSection.id,
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

        if (state.ribbon.isReorderDialogShown) {
            SelectRibbonStatusReorderDialog(
                onDismissRequest = {
                    actions.onChangeRibbonStatusClosed()
                },
                onDoneClicked = {
                    actions.onSelectedRibbonDataChanged(statuses = it)
                    actions.onChangeRibbonStatusClosed()
                },
                statuses = state.ribbon.statuses,
            )
        }

        if (state.notifications.isTimePickerShown) {
            TimePickerDialog(
                time = state.notifications.time,
                sheetState = sheetState,
                onSaveTime = { hours, minutes ->
                    actions.onNotificationTimeSaved(hours = hours, minutes = minutes)
                },
                onDismissRequest = {
                    actions.onNotificationTimePickerClosed()
                }
            )
        }

        if (state.cache.isDialogShown) {
            ListPreferencesDialog(
                values = (7..13).map { 1 shl it },
                selectedValue = state.cache.maxImageSize,
                valueText = { formatSize(it * 1024 * 1024L) },
                onValueSelected = { size ->
                    actions.onMaxImageCacheSize(size)
                },
                onDismiss = {
                    actions.onMaxImageCacheSizeDialogClosed()
                },
            )
        }

        if (state.theme.isDialogShown) {
            ListPreferencesDialog(
                values = ThemeMode.entries,
                selectedValue = state.theme.mode,
                valueText = { stringResource(it.fromMode()) },
                onValueSelected = { mode ->
                    actions.onThemeSelected(mode)
                },
                onDismiss = {
                    actions.onThemeDialogClosed()
                },
            )
        }
    }
}

fun formatSize(sizeBytes: Long): String {
    val prefix = if (sizeBytes < 0) "-" else ""
    var result: Long = sizeBytes.absoluteValue
    var suffix = "B"
    if (result > 900) {
        suffix = "KB"
        result /= 1024
    }
    if (result > 900) {
        suffix = "MB"
        result /= 1024
    }
    if (result > 900) {
        suffix = "GB"
        result /= 1024
    }
    if (result > 900) {
        suffix = "TB"
        result /= 1024
    }

    return "$prefix$result $suffix"
}

fun ThemeMode.fromMode(): Int {
    return when (this) {
        ThemeMode.SYSTEM -> R.string.settings_theme_system
        ThemeMode.LIGHT -> R.string.settings_theme_light
        ThemeMode.DARK -> R.string.settings_theme_dark
        ThemeMode.AMOLED -> R.string.settings_theme_amoled
    }
}
