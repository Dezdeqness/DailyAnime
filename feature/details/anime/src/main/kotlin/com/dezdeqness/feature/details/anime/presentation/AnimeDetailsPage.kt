package com.dezdeqness.feature.details.anime.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.details.anime.presentation.preview.AnimeDetailsPreviewData
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsContent
import com.dezdeqness.feature.details.common.presentation.DetailsPage
import com.dezdeqness.feature.details.common.presentation.composables.DetailsLoadingSkeleton
import com.dezdeqness.feature.details.common.presentation.composables.ShimmerRow
import com.dezdeqness.feature.details.common.presentation.utils.LocalDetailsRenderManagerComposition
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AnimeDetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeDetailsNamespace.State>,
    onUiEvent: (AnimeDetailsUiEvent) -> Unit,
    onBackPressed: () -> Unit,
) {
    val manager = remember {
        animeDetailsRenderManager()
    }
    CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
        DetailsPage(
            modifier = modifier,
            stateFlow = stateFlow,
            onUiEvent = onUiEvent,
            onShare = { onUiEvent(AnimeDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed)) },
            onRetry = { onUiEvent(AnimeDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked)) },
            onBack = onBackPressed,
            onFavouriteClick = { onUiEvent(AnimeDetailsUiEvent.Base(DetailsBaseUiEvent.FavouriteToggled)) },
            loading = { modifier ->
                DetailsLoadingSkeleton(modifier = modifier) {
                    ShimmerRow(
                        itemSize = 60.dp,
                        itemWidth = 60.dp,
                        itemHeight = 20.dp,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    )
                    ShimmerRow(
                        itemSize = 120.dp,
                        itemWidth = 120.dp,
                        itemHeight = 170.dp,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    )
                }
            },
            overlay = { state, dispatch -> AnimeDetailsFab(state, dispatch) },
        )
    }
}

@PreviewLightDark
@Composable
fun AnimeDetailsContentPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            val manager = remember {
                animeDetailsRenderManager()
            }
            CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
                DetailsContent<AnimeDetailsUiEvent>(
                    sections = AnimeDetailsPreviewData.sections,
                    onEvent = {},
                )
            }
        }
    }
}
