package com.dezdeqness.presentation.features.genericlistscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.features.details.composables.list.DetailsError
import com.dezdeqness.presentation.features.genericlistscreen.composable.GenericList
import com.dezdeqness.presentation.features.genericlistscreen.composable.GenericListLoading
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericListPage(
    modifier: Modifier = Modifier,
    title: String,
    stateFlow: StateFlow<GenericListableState>,
    actions: GenericListableActions,
) {
    val state by stateFlow.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                navigationClick = actions::onBackPressed,
            )
        },
        containerColor = AppTheme.colors.onPrimary,
    ) { padding ->
        Box(modifier = modifier
            .padding(padding)
            .fillMaxSize()) {

            when (state.status) {
                GenericListableStatus.Loading, GenericListableStatus.Initial -> {
                    GenericListLoading(modifier = Modifier.fillMaxSize())
                }

                GenericListableStatus.Loaded -> {
                    GenericList(
                        list = state.list,
                        onClick = { action -> actions.onActionReceive(action) }
                    )
                }

                GenericListableStatus.Error -> {
                    DetailsError(onRetryClick = actions::onRetryClicked)
                }
            }
        }
    }
}
