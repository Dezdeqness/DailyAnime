package com.dezdeqness.presentation.features.history


import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent

class HistoryFragment : BaseComposeFragment() {

    private val viewModel: HistoryViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun setupScreenComponent(component: AppComponent) {
        component
            .historyComponent()
            .create()
            .inject(this)
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {

        }
    }

}
