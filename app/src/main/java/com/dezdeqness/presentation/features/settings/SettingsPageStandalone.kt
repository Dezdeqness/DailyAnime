package com.dezdeqness.presentation.features.settings

import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.domain.model.InitialSection
import com.dezdeqness.presentation.event.OpenSettingsAlarm
import com.dezdeqness.presentation.event.SwitchDarkTheme
import com.dezdeqness.presentation.features.debugscreen.DebugScreenActivity
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import androidx.core.net.toUri

@Composable
fun SettingsPageStandalone(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val settingsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .settingsComponent()
            .create()
    }

    val viewModel = viewModel<SettingsViewModel>(factory = settingsComponent.viewModelFactory())

    SettingsPage(
        modifier = modifier,
        stateFlow = viewModel.settingsStateFlow,
        actions = object : SettingActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onNightThemeToggleClicked(isChecked: Boolean) {
                viewModel.onNightThemeToggleChecked(isChecked)
            }

            override fun onChangeInitialSectionClicked() {
                viewModel.onChangeInitialSectionClicked()
            }

            override fun onSelectedSectionChanged(section: InitialSection) {
                viewModel.onSelectedSectionChanged(section)
            }

            override fun onSelectedSectionDialogClosed() {
                viewModel.onSelectedSectionDialogClosed()
            }

            override fun onChangeRibbonStatusClicked() {
                viewModel.onChangeRibbonStatusClicked()
            }

            override fun onSelectedRibbonDataChanged(statuses: List<RibbonStatusUiModel>) {
                viewModel.onSelectedRibbonDataChanged(statuses)
            }

            override fun onChangeRibbonStatusClosed() {
                viewModel.onChangeRibbonStatusClosed()
            }

            override fun onNotificationToggleClicked(isEnabled: Boolean) {
                viewModel.onNotificationToggleClicked(isEnabled = isEnabled)
            }

            override fun onNotificationTimePickerClicked() {
                viewModel.onNotificationTimeClicked()
            }

            override fun onNotificationTimeSaved(hours: Int, minutes: Int) {
                viewModel.onNotificationTimeSaved(hours, minutes)
            }

            override fun onNotificationTimePickerClosed() {
                viewModel.onNotificationTimePickerClosed()
            }

            override fun onMaxImageCacheSizeClicked() {
                viewModel.onMaxImageCacheSizeClicked()
            }

            override fun onMaxImageCacheSize(size: Int) {
                viewModel.onMaxImageCacheSize(size = size)
            }

            override fun onMaxImageCacheSizeDialogClosed() {
                viewModel.onMaxImageCacheSizeDialogClosed()
            }

            override fun invalidate() {
                viewModel.invalidate()
            }

            override fun onDebugOptionsClicked() {
                context.startActivity(DebugScreenActivity.newIntent(context = context))
            }
        }
    )

    viewModel.events.collectEvents { event ->
        when (event) {
            is SwitchDarkTheme -> {
                val themeMode = if (event.isEnabled) {
                    MODE_NIGHT_YES
                } else {
                    MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(themeMode)
            }

            is OpenSettingsAlarm -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                }
            }
            else -> {}
        }
    }
}
