package com.dezdeqness.presentation.features.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.details.composables.DetailsList
import com.dezdeqness.presentation.features.details.composables.DetailsToolbar
import com.dezdeqness.presentation.features.details.composables.ShimmerDetailsLoading
import com.dezdeqness.presentation.features.details.composables.list.DetailsError
import com.dezdeqness.presentation.features.userrate.UserRateDialog
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeDetailsState>,
    actions: DetailsActions,
) {
    val state by stateFlow.collectAsState()

    val listState = rememberLazyListState()
    val scrollIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val isScrolledPast = remember(scrollIndex) { scrollIndex >= 1 }

    val toolbarBackgroundColor =
        if (isScrolledPast) AppTheme.colors.onPrimary else Color.Transparent

    Box(modifier = modifier.fillMaxSize()) {

        when (state.status) {
            DetailsStatus.Loading, DetailsStatus.Initial -> {
                ShimmerDetailsLoading(modifier = Modifier.fillMaxSize())
            }

            DetailsStatus.Loaded -> {
                DetailsList(
                    list = state.uiModels,
                    state = listState,
                    onClick = { action -> actions.onActionReceive(action) }
                )

                val editRateBottomSheet = state.currentBottomSheet as? BottomSheet.EditRate

                if (editRateBottomSheet != null) {
                    UserRateDialog(
                        onClosed = {
                            actions.onUserRateBottomDialogClosed()
                        },
                        userRateId = editRateBottomSheet.userRateId,
                        title = editRateBottomSheet.title,
                        onSaveClicked = actions::onUserRateChanged,
                    )
                }
            }

            DetailsStatus.Error -> {
                DetailsError(onRetryClick = actions::onRetryClicked)
            }
        }

        DetailsToolbar(
            isLoaded = state.uiModels.isNotEmpty(),
            toolbarColor = toolbarBackgroundColor,
            onBackClick = actions::onBackPressed,
            onShareClick = actions::onSharePressed,
        )

        if (state.isEditRateFabShown) {
            FloatingActionButton(
                onClick = actions::onFabClicked,
                containerColor = AppTheme.colors.accent,
                contentColor = AppTheme.colors.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Icon(
                    painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface,
                )
            }
        }
    }
}
