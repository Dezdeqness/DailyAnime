package com.dezdeqness.presentation.features.animedetails

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsPage
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsUiEvent
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsViewModel
import com.dezdeqness.feature.details.anime.presentation.AnimeIdKey
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.anime.presentation.store.EditRateSheetState
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.CharacterDetails
import com.dezdeqness.presentation.Chronology
import com.dezdeqness.presentation.DetailsStats
import com.dezdeqness.presentation.Screenshots
import com.dezdeqness.presentation.Similar
import com.dezdeqness.presentation.features.animestats.AnimeStatsTransferModel
import com.dezdeqness.presentation.features.animestats.serializeListToString
import com.dezdeqness.presentation.features.userrate.UserRateDialogStandalone
import kotlinx.coroutines.launch

@Composable
fun AnimeDetailsStandalonePage(
    modifier: Modifier = Modifier,
    animeId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val component = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .animeDetailsFeatureComponent()
            .create()
    }

    val owner = LocalViewModelStoreOwner.current
    val extras = if (owner is HasDefaultViewModelProviderFactory) {
        owner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }

    val viewModel = viewModel<AnimeDetailsViewModel>(
        key = "anime_details_$animeId",
        factory = component.viewModelFactory(),
        extras = MutableCreationExtras(extras).apply { set(AnimeIdKey, animeId) },
    )

    val messageConsumer = remember { component.messageConsumer() }
    val messageProvider = remember { component.messageProvider() }
    val dispatcherProvider = remember { component.coroutineDispatcherProvider() }
    val scope = rememberCoroutineScope()
    val baseUrl = remember {
        (context.applicationContext as ShikimoriApp).appComponent.configManager.baseUrl.trimEnd('/')
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    AnimeDetailsPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        onUiEvent = viewModel::onUiEvent,
        onBackPressed = { navController.popBackStack() },
    )

    val sheet = state.editRateSheet
    if (sheet is EditRateSheetState.Visible && state.status == DetailsStatus.Loaded) {
        UserRateDialogStandalone(
            userRateId = sheet.userRateId,
            title = sheet.title,
            onSaveClicked = { viewModel.onUiEvent(AnimeDetailsUiEvent.SaveUserRate(it)) },
            onClosed = { viewModel.onUiEvent(AnimeDetailsUiEvent.EditRateClosed) },
        )
    }

    viewModel.effects.collectEvents { effect ->
        when (effect) {
            is AnimeDetailsNamespace.Effect.Base -> when (val base = effect.effect) {
                is BaseDetailsEffect.Error -> {
                    scope.launch(dispatcherProvider.io()) {
                        messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                    }
                }
                is BaseDetailsEffect.Share -> {
                    val absolute = if (base.url.startsWith(baseUrl)) base.url else baseUrl + base.url
                    ShareCompat.IntentBuilder(context)
                        .setType("text/plain")
                        .setText(absolute)
                        .startChooser()
                }
                is BaseDetailsEffect.FavouriteActionFailed -> {
                    scope.launch(dispatcherProvider.io()) {
                        messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                    }
                }
            }
            is AnimeDetailsNamespace.Effect.EditRateError -> {
                scope.launch(dispatcherProvider.io()) {
                    messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
                }
            }
            is AnimeDetailsNamespace.Effect.EditRateCreated -> {
                scope.launch(dispatcherProvider.io()) {
                    messageConsumer.onSuccessMessage(messageProvider.getAnimeEditCreateSuccessMessage())
                }
            }
            is AnimeDetailsNamespace.Effect.EditRateUpdated -> {
                scope.launch(dispatcherProvider.io()) {
                    messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
                }
            }
            is AnimeDetailsNamespace.Effect.OpenVideo -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, effect.url.toUri()))
            }
            is AnimeDetailsNamespace.Effect.NavigateToAnime -> {
                navController.navigate(AnimeDetails(effect.animeId))
            }
            is AnimeDetailsNamespace.Effect.NavigateToCharacter -> {
                navController.navigate(CharacterDetails(effect.characterId))
            }
            is AnimeDetailsNamespace.Effect.NavigateToSimilar -> {
                navController.navigate(Similar(effect.animeId))
            }
            is AnimeDetailsNamespace.Effect.NavigateToChronology -> {
                navController.navigate(Chronology(effect.animeId))
            }
            is AnimeDetailsNamespace.Effect.NavigateToStats -> {
                navController.navigate(
                    DetailsStats(
                        scoreString = serializeListToString(
                            effect.scores.map { AnimeStatsTransferModel(it.name, it.value) },
                        ),
                        statusesString = serializeListToString(
                            effect.statuses.map { AnimeStatsTransferModel(it.name, it.value) },
                        ),
                    ),
                )
            }
            is AnimeDetailsNamespace.Effect.NavigateToScreenshotViewer -> {
                navController.navigate(
                    Screenshots(screenshots = effect.urls, index = effect.index),
                )
            }
        }
    }
}
