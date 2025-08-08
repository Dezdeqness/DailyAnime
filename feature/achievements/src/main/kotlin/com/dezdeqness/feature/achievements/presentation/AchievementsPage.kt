package com.dezdeqness.feature.achievements.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.feature.achievements.R
import com.dezdeqness.feature.achievements.presentation.composable.AchievementsEmpty
import com.dezdeqness.feature.achievements.presentation.composable.AchievementsError
import com.dezdeqness.feature.achievements.presentation.composable.AchievementsList
import com.dezdeqness.feature.achievements.presentation.composable.ShimmerAchievementsLoading
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AchievementsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AchievementsUiState>,
    actions: AchievementsActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.toolbar_title),
                navigationClick = {
                    actions.onBackPressed()
                }
            )
        },
    ) { contentPadding ->
        Box(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            when (state.status) {
                Status.Initial, Status.Loading -> {
                    ShimmerAchievementsLoading(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                Status.Error -> {
                    AchievementsError()
                }

                Status.Empty -> {
                    AchievementsEmpty()
                }

                Status.Loaded -> {
                    AchievementsList(
                        common = state.common,
                        genres = state.genres,
                    )
                }

            }

        }
    }
}
