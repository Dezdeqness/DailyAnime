package com.dezdeqness.presentation.features.genericlistscreen

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer

@Composable
fun GenericListableScreen(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    renderer: GenericRenderer,
    viewModel: GenericListableViewModel,
    onEvent: (Event) -> Boolean = { false },
    navController: NavHostController,
) {
    val context = LocalContext.current

    val eventConsumer = remember {
        EventConsumer(context = context)
    }

    CompositionLocalProvider(LocalAdapterItemRenderer provides renderer) {
        GenericListPage(
            modifier = modifier,
            title = stringResource(titleRes),
            stateFlow = viewModel.genericListableStateFlow,
            actions = object : GenericListableActions {
                override fun onActionReceive(action: Action) {
                    viewModel.onActionReceive(action)
                }

                override fun onRetryClicked() {
                    viewModel.onRetryButtonClicked()
                }

                override fun onBackPressed() {
                    navController.popBackStack()
                }
            },
        )
    }

    viewModel.events.collectEvents { event ->
        val isConsumed = onEvent(event)
        if (isConsumed) {
            return@collectEvents
        }

        when (event) {
            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }
    }

}
