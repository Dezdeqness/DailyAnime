package com.dezdeqness.feature.personallist.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.views.GeneralEmpty
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.feature.personallist.search.composables.PersonalListTabs
import com.dezdeqness.feature.personallist.search.composables.PersonalSearchList
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PersonalListSearchExpandedPage(
    stateFlow: StateFlow<PersonalListSearchState>,
    actions: PersonalListSearchActions,
    modifier: Modifier = Modifier,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {

        when (state.status) {
            PersonalListSearchStatus.Loaded -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    PersonalListTabs(
                        selectedTab = state.selectedTab,
                        onTabSelected = actions::onTabSelect,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PersonalSearchList(
                        list = state.list,
                        onAnimeClick = actions::onAnimeClick,
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
