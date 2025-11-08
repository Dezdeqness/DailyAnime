package com.dezdeqness.presentation.features.userrate_action

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRateActionDialog(
    modifier: Modifier = Modifier,
    userRateId: Long,
    onClosed: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        },
    )
    val context = LocalContext.current
    val userRateActionComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .userRateActionComponent()
            .build()
    }

    val viewModel =
        viewModel<UserRateActionViewModel>(factory = userRateActionComponent.viewModelFactory())

    LaunchedEffect( userRateId) {
        viewModel.onUserRateUpdated(rateId = userRateId)
    }

    val actions = remember {
        object :UserRateActionActions {
            override fun onAction(action: UserRateActions) {
            }

        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onClosed() },
        sheetState = sheetState,
        sheetGesturesEnabled = true,
    ) {
        UserRateActionContent(
            modifier = modifier,
            stateFlow = ,
            actions = actions,
        )
    }

}
