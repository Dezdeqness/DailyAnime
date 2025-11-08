package com.dezdeqness.presentation.features.userrate_action

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.views.buttons.ContextButton
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserRateActionContent(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<ImmutableList<UserRateActions>>,
    actions: UserRateActionActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Column(modifier) {
        state.forEachIndexed { index, action ->
            ContextButton(
                title = stringResource(action.title),
                icon = action.icon,
                onClick = {
                    actions.onAction(action)
                }
            )
        }
    }
}
