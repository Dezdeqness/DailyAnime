package com.dezdeqness.presentation.features.stats

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.page.StatsPage
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent

class StatsFragment : BaseComposeFragment() {

    private val viewModel: StatsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun setupScreenComponent(component: AppComponent) {
        component
            .statsComponent()
            .create()
            .inject(this)
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            MaterialTheme {
                StatsPage(
                    state = viewModel.statsStateFlow,
                    onBackPressed = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }

}
