package com.dezdeqness.feature.personallist.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.GeneralEmpty
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.feature.personallist.search.composables.PersonalListTabs
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PersonalListSearchExpandedPage(
    stateFlow: StateFlow<PersonalListSearchState>,
    onTabSelected: (PersonalListTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.background(AppTheme.colors.onPrimary)) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (state.status) {
                PersonalListSearchStatus.Loaded -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        PersonalListTabs(
                            selectedTab = state.selectedTab,
                            onTabSelected = onTabSelected,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                PersonalListSearchStatus.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                PersonalListSearchStatus.Empty -> {
                    GeneralEmpty(modifier = Modifier.align(Alignment.Center))
                }

                PersonalListSearchStatus.Error -> {
                    GeneralError(modifier = Modifier.align(Alignment.Center))
                }

                PersonalListSearchStatus.Initial -> {
                }
            }
        }
    }
}
