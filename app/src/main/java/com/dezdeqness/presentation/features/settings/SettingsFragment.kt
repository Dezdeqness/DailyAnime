package com.dezdeqness.presentation.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentSettingsBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.event.SwitchDarkTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )


    @Inject
    protected lateinit var settingsRepository: SettingsRepository

    override fun setupScreenComponent(component: AppComponent) {
        component
            .settingsComponent()
            .create()
            .inject(this)
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingsStateFlow.collect { state ->
                    binding.toggle.isChecked = state.isDarkThemeEnabled

                    state.events.forEach { event ->
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
                        viewModel.onEventConsumed(event)
                    }
                }
            }
        }
    }

    private fun setupViews() {
        binding.toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.onNightThemeToggleChecked(isChecked)
            }
        }
    }

    private fun setupToolbar() {
        with(binding.historyToolbar) {
            title = context.getString(R.string.settings_toolbar_title)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }


}
