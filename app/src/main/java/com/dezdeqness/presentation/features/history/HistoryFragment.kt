package com.dezdeqness.presentation.features.history


import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.collectEvents
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.features.history.store.HistoryNamespace

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
            HistoryPage(
                stateFlow = viewModel.state,
                actions = object : HistoryActions {
                    override fun onPullDownRefreshed() {
                        viewModel.onPullDownRefreshed()
                    }

                    override fun onLoadMore() {
                        viewModel.onLoadMore()
                    }

                    override fun onBackPressed() {
                        findNavController().popBackStack()
                    }
                }
            )

            viewModel.effects.collectEvents {
                when (it) {
                    HistoryNamespace.Effect.Error -> {
                        viewModel.onErrorMessage()
                    }
                }
            }
        }
    }

}
