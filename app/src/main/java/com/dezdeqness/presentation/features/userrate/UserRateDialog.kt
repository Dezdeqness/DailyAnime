package com.dezdeqness.presentation.features.userrate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.presentation.event.EditUserRate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRateDialog(
    modifier: Modifier = Modifier,
    userRateId: Long,
    title: String,
    onSaveClicked: (EditRateUiModel) -> Unit,
    onClosed: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        },
    )

    val context = LocalContext.current
    val editRateComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .editRateComponent()
            .build()
    }

    val viewModel =
        viewModel<UserRateViewModel>(factory = editRateComponent.viewModelFactory())

    LaunchedEffect(title, userRateId) {
        viewModel.onUserRateUpdated(rateId = userRateId, title = title)
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onClosed() },
        sheetState = sheetState,
        dragHandle = null,
        sheetGesturesEnabled = false,
        properties = ModalBottomSheetProperties()
    ) {
        UserRateContent(
            stateFlow = viewModel.userRateStateFlow,
            actions = object : UserRateActions {
                override fun onStatusChanged(id: String) {
                    viewModel.onStatusChanged(id)
                }

                override fun onScoreChanged(score: Long) {
                    viewModel.onScoreChanged(score)
                }

                override fun onSelectStatusClicked() {
                    viewModel.onSelectStatusClicked()
                }

                override fun onCloseSelectStatusClicked() {
                    viewModel.onCloseSelectStatusClicked()
                }

                override fun onResetClicked() {
                    viewModel.onResetButtonClicked()
                }

                override fun onChangeRateClicked() {
                    viewModel.onApplyButtonClicked()
                }

                override fun onBackPressed() {
                    onClosed()
                }

                override fun onIncrementEpisode() {
                    viewModel.onEpisodesPlusClicked()
                }

                override fun onDecrementEpisode() {
                    viewModel.onEpisodesMinusClicked()
                }

                override fun onEpisodesChanged(episodes: String) {
                    viewModel.onEpisodesChanged(episodes)
                }

                override fun onCommentChanged(comment: String) {
                    viewModel.onCommentChanged(comment)
                }
            }
        )

    }

    viewModel.events.collectEvents { event ->
        when (event) {
            is EditUserRate -> {
                onSaveClicked(event.userRateUiModel)
                onClosed()
            }

            else -> {}
        }
    }
}
