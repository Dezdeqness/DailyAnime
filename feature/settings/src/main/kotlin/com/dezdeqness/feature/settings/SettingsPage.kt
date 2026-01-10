package com.dezdeqness.feature.settings

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.feature.settings.composables.HeaderCustomSettingsView
import com.dezdeqness.feature.settings.composables.SwitchSettingsView
import com.dezdeqness.feature.settings.composables.TextSettingsView
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun SettingsPage(
    stateFlow: StateFlow<SettingsNamespace.State>,
    modifier: Modifier = Modifier,
    actions: SettingActions
) {
    val context = LocalContext.current

    val state by stateFlow.collectAsStateWithLifecycle()

    val items = state.settings

    val imageDiskCache = context.imageLoader.diskCache ?: return

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

//    val listSections = remember(state.isCalendarEnabled) {
//        SelectSectionItem.getSections(state.isCalendarEnabled)
//    }

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

            items(
                count = items.size,
                key = { index -> items[index].uniqueId },
            ) { index ->
                when (val item = items[index]) {
                    is SettingUiPref.HeaderSetting -> {
                        HeaderCustomSettingsView(title = item.getTitle())
                    }

                    is SettingUiPref.ActionLessSetting -> {
                        TextSettingsView(
                            title = item.getTitle(),
                            subtitle = item.getSubtitle(),
                        )
                    }

                    is SettingUiPref.ActionSetting -> {
                        TextSettingsView(
                            title = item.getTitle(),
                            subtitle = item.getSubtitle(),
                            enabled = item.enabled,
                            suffixIcon = if (item.isPostfixShown) {
                                {
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = AppTheme.colors.textPrimary,
                                    )
                                }
                            } else {
                                null
                            },
                            onClick = {
                                actions.onSettingClicked(item.id)
                            }
                        )
                    }

                    is SettingUiPref.SwitchSetting -> {
                        SwitchSettingsView(
                            title = item.getTitle(),
                            subtitle = item.getSubtitle(),
                            checked = item.checked,
                            enabled = item.enabled,
                        ) { isChecked ->
                            actions.onSwitchChanged(item.id, isChecked)
                        }
                    }

                    else -> {

                    }
                }
            }
//
//            item {
//                SettingsSection(
//                    sectionTitle = stringResource(R.string.settings_storage_title),
//                    content = {
//                        var imageCacheSize by remember(state.cache.maxImageSize) {
//                            mutableLongStateOf(imageDiskCache.size)
//                        }
//
//                        LaunchedEffect(imageDiskCache) {
//                            while (isActive) {
//                                delay(500)
//                                imageCacheSize = imageDiskCache.size
//                            }
//                        }
//
//                        val imageCacheProgress by animateFloatAsState(
//                            targetValue = (imageCacheSize.toFloat() / imageDiskCache.maxSize).coerceIn(
//                                0f,
//                                1f
//                            ),
//                            label = ""
//                        )
//                        ProgressSettingsView(
//                            title = stringResource(R.string.settings_image_cache_title),
//                            subtitle = stringResource(
//                                R.string.settings_image_cache_used_title,
//                                formatSize(imageCacheSize),
//                                formatSize(imageDiskCache.maxSize)
//                            ),
//                            progress = imageCacheProgress,
//                        )
//
//                        TextSettingsView(
//                            title = stringResource(R.string.settings_max_image_cache_title),
//                            subtitle = formatSize(imageDiskCache.maxSize),
//                            onClick = {
//                                actions.onMaxImageCacheSizeClicked()
//                            }
//                        )
//
//                        TextSettingsView(
//                            title = stringResource(R.string.settings_image_cache_clear_title),
//                            onClick = {
//                                imageDiskCache.clear()
//                            }
//                        )
//                    }
//                )
//            }
        }

//        if (state.navigation.isDialogShown) {
//            SelectSectionDialog(
//                selectedId = state.navigation.selectedSection.id,
//                state = sheetState,
//                statuses = listSections,
//                onSelectedItem = {
//                    actions.onSelectedSectionChanged(it)
//                },
//                onCloseClicked = {
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (sheetState.isVisible.not()) {
//                            actions.onSelectedSectionDialogClosed()
//                        }
//                    }
//                }
//            )
//        }

//        if (state.ribbon.isReorderDialogShown) {
//            SelectRibbonStatusReorderDialog(
//                onDismissRequest = {
//                    actions.onChangeRibbonStatusClosed()
//                },
//                onDoneClicked = {
//                    actions.onSelectedRibbonDataChanged(statuses = it)
//                    actions.onChangeRibbonStatusClosed()
//                },
//                statuses = state.ribbon.statuses,
//            )
//        }

//        if (state.notifications.isTimePickerShown) {
//            TimePickerDialog(
//                time = state.notifications.time,
//                sheetState = sheetState,
//                onSaveTime = { hours, minutes ->
//                    actions.onNotificationTimeSaved(hours = hours, minutes = minutes)
//                },
//                onDismissRequest = {
//                    actions.onNotificationTimePickerClosed()
//                }
//            )
//        }
//
//        if (state.cache.isDialogShown) {
//            ListPreferencesDialog(
//                values = (7..13).map { 1 shl it },
//                selectedValue = state.cache.maxImageSize,
//                valueText = { formatSize(it * 1024 * 1024L) },
//                onValueSelected = { size ->
//                    actions.onMaxImageCacheSize(size)
//                },
//                onDismiss = {
//                    actions.onMaxImageCacheSizeDialogClosed()
//                },
//            )
//        }

//        if (state.theme.isDialogShown) {
//            ListPreferencesDialog(
//                values = ThemeMode.entries,
//                selectedValue = state.theme.mode,
//                valueText = { stringResource(it.fromMode()) },
//                onValueSelected = { mode ->
//                    actions.onThemeSelected(mode)
//                },
//                onDismiss = {
//                    actions.onThemeDialogClosed()
//                },
//            )
//        }
    }
}

