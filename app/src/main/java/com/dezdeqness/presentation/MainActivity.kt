package com.dezdeqness.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.BackFragmentListener
import com.dezdeqness.databinding.ActivityMainBinding
import com.dezdeqness.extensions.setupWithNavController
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.LanguageDisclaimer
import com.dezdeqness.presentation.event.OpenCalendarTab
import com.dezdeqness.ui.ShikimoriSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity(), TabSelection {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application
            .getComponent()
            .mainComponent()
            .create()
            .inject(this)

        lifecycleScope.launch {
            val status = application.getComponent().settingsRepository().getNightThemeStatus()
            if (status) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setupObservers()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.fragments.firstOrNull { it.isAdded && it.isVisible }
            ?: return

        val lastFragment =
            fragment.childFragmentManager.fragments.filterIsInstance<BackFragmentListener>()
                .lastOrNull()
        val isBackOk = lastFragment?.isBackNeed() ?: true

        if (isBackOk) {
            super.onBackPressed()
        } else {
            lastFragment?.onBackPressed()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNav = binding.navigation

        val navGraphIds = listOf(
            R.navigation.personal_host_nav_graph,
            R.navigation.home_nav_graph,
            R.navigation.calendar_nav_graph,
            R.navigation.search_nav_graph,
            R.navigation.profile_nav_graph,
        )

        lifecycleScope.launch {
            val id = withContext(application.getComponent().coroutineDispatcherProvider().io()) {
                application.getComponent().settingsRepository().getSelectedInitialSection()
            }

            bottomNav.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.container,
                intent = intent,
                selectedGraphId = id
            )
        }

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.messageState.collect { event ->
                    ShikimoriSnackbar
                        .showSnackbarShort(
                            view = binding.root,
                            anchorView = binding.navigation,
                            event = event,
                        )
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.events.collect { event ->
                    when (event) {
                        is LanguageDisclaimer -> {
                            showLanguageDisclaimer()
                        }
                        is OpenCalendarTab -> {
                            binding.navigation.selectedItemId = R.id.calendar_nav_graph
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLanguageDisclaimer() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.language_disclaimer_title))
            .setMessage(resources.getString(R.string.language_disclaimer_description))
            .setPositiveButton(resources.getString(R.string.language_disclaimer_positive)) { _, _ ->

            }
            .show()
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun navigateToCalendarTab() {
        mainViewModel.onNavigateToCalendarTab()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}
