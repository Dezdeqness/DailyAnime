package com.dezdeqness.presentation.features.stats

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.dezdeqness.core.BaseComposeFragment
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

        }
    }

}
