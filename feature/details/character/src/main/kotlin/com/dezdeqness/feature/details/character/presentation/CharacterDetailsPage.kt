package com.dezdeqness.feature.details.character.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.character.presentation.preview.CharacterDetailsPreviewData
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsContent
import com.dezdeqness.feature.details.common.presentation.DetailsPage
import com.dezdeqness.feature.details.common.presentation.composables.DetailsLoadingSkeleton
import com.dezdeqness.feature.details.common.presentation.composables.ShimmerRow
import com.dezdeqness.feature.details.common.presentation.utils.LocalDetailsRenderManagerComposition
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CharacterDetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<CharacterDetailsNamespace.State>,
    onUiEvent: (CharacterDetailsUiEvent) -> Unit,
    onBackPressed: () -> Unit,
) {
    val manager = remember { characterDetailsRenderManager() }

    CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
        DetailsPage(
            modifier = modifier,
            stateFlow = stateFlow,
            onUiEvent = onUiEvent,
            onShare = { onUiEvent(CharacterDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed)) },
            onRetry = { onUiEvent(CharacterDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked)) },
            onBack = onBackPressed,
            onFavouriteClick = { onUiEvent(CharacterDetailsUiEvent.Base(DetailsBaseUiEvent.FavouriteToggled)) },
            loading = { modifier ->
                DetailsLoadingSkeleton(modifier = modifier) {
                    ShimmerRow(
                        itemSize = 80.dp,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    )
                }
            },
        )
    }
}

@PreviewLightDark
@Composable
fun CharacterDetailsContentPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            val manager = remember { characterDetailsRenderManager() }

            CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
                DetailsContent<CharacterDetailsUiEvent>(
                    sections = CharacterDetailsPreviewData.sections,
                    onEvent = {},
                )
            }
        }
    }
}
