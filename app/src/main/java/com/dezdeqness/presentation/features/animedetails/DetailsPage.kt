package com.dezdeqness.presentation.features.animedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dezdeqness.presentation.features.animedetails.composables.DetailsList
import com.dezdeqness.presentation.features.animedetails.composables.DetailsToolbar
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeDetailsState>,
    actions: DetailsActions,
) {
    val state by stateFlow.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {

        DetailsList(
            list = state.uiModels,
        )

        DetailsToolbar(
            isLoaded = state.uiModels.isNotEmpty(),
            toolbarColor = Color.Transparent,
            onBackClick = actions::onBackPressed,
            onShareClick = actions::onSharePressed,
        )
    }
}
