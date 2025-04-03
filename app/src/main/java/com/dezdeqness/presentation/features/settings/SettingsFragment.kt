package com.dezdeqness.presentation.features.settings

import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.manager.AppThemeManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.event.SwitchDarkTheme
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : BaseComposeFragment() {

    private val viewModel: SettingsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    @Inject
    lateinit var appThemeManager: AppThemeManager

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun setupScreenComponent(component: AppComponent) {
        component
            .settingsComponent()
            .create()
            .inject(this)
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            SettingsPage(
                stateFlow = viewModel.settingsStateFlow,
                actions = object : SettingActions {
                    override fun onBackPressed() {
                        findNavController().navigateUp()
                    }

                    override fun onNightThemeToggleClicked(isChecked: Boolean) {
                        viewModel.onNightThemeToggleChecked(isChecked)
                    }

                    override fun onChangeInitialSectionClicked() {
                        viewModel.onChangeInitialSectionClicked()
                    }

                    override fun onSelectedSectionChanged(sectionId: Int) {
                        viewModel.onSelectedSectionChanged(sectionId)
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

                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is SwitchDarkTheme -> {
                            val themeMode = if (event.isEnabled) {
                                MODE_NIGHT_YES
                            } else {
                                MODE_NIGHT_NO
                            }
                            AppCompatDelegate.setDefaultNightMode(themeMode)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

}
