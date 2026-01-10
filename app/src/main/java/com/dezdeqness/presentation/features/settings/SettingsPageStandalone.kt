package com.dezdeqness.presentation.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.feature.settings.SettingActions
import com.dezdeqness.feature.settings.SettingsPage
import com.dezdeqness.feature.settings.SettingsViewModel
import com.dezdeqness.feature.settings.store.actors.OpenDebugMenu
import com.dezdeqness.feature.settings.store.actors.OpenSelectInterests
import com.dezdeqness.presentation.SelectGenres
import com.dezdeqness.presentation.features.debugscreen.DebugScreenActivity

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
        stateFlow = viewModel.state,
        actions = object : SettingActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onSettingClicked(id: String) {
                viewModel.onSettingClicked(id)
            }

            override fun onSwitchChanged(id: String, checked: Boolean) {
                viewModel.onSwitchChanged(id, checked)
            }

            override fun invalidate() {
                viewModel.invalidate()
            }

        }
    )

    viewModel.effects.collectEvents { effect ->
        when (effect) {
            is OpenDebugMenu -> {
                context.startActivity(DebugScreenActivity.newIntent(context = context))
            }
//
//            is OpenSettingsAlarm -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
//                        data = "package:${context.packageName}".toUri()
//                    }
//                    context.startActivity(intent)
//                }
//            }
//
            is OpenSelectInterests -> {
                navController.navigate(SelectGenres)
            }

            else -> {}
        }
    }
}
