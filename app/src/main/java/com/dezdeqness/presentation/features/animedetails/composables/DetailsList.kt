package com.dezdeqness.presentation.features.animedetails.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsCharacters
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsDescription
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsGenres
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsHeader
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsRelated
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsScreenshots
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsTitle
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsVideos
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeCellList
import com.dezdeqness.presentation.models.DescriptionUiModel
import com.dezdeqness.presentation.models.HeaderItemUiModel
import com.dezdeqness.presentation.models.NameUiModel
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RoleUiModelList
import com.dezdeqness.presentation.models.ScreenshotUiModelList
import com.dezdeqness.presentation.models.VideoUiModelList
import com.google.common.collect.ImmutableList

@Composable
fun DetailsList(
    modifier: Modifier = Modifier,
    list: ImmutableList<AdapterItem>,
    state: LazyListState = rememberLazyListState(),
    onClick: (Action) -> Unit
) {
    LazyColumn(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id() + item.hashCode()
            }
        ) { index ->
            val item = list[index]

            when (item) {
                is HeaderItemUiModel -> {
                    DetailsHeader(detailsHeader = item)
                }
                is AnimeCellList -> {
                    DetailsGenres(genreCells = item)
                }
                is DescriptionUiModel -> {
                    DetailsDescription(
                        description = item,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                is NameUiModel -> {
                    DetailsTitle(
                        title = item.title,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                is ScreenshotUiModelList -> {
                    DetailsScreenshots(
                        screenshots = item.list,
                        onScreenshotClick = {
                            onClick(Action.ScreenShotClick(it))
                        }
                    )
                }
                is VideoUiModelList -> {
                    DetailsVideos(
                        videos = item.list,
                        onVideoClick = {
                            onClick(Action.VideoClick(it))
                        },
                    )
                }
                is RoleUiModelList -> {
                    DetailsCharacters(characters = item.list)
                }
                is RelatedItemListUiModel -> {
                    DetailsRelated(
                        relatedList = item.list,
                        onRelatedClick = { id ->
                            onClick(Action.AnimeClick(id))
                        }
                    )
                }
                else -> {
                }
            }
        }

    }
}
