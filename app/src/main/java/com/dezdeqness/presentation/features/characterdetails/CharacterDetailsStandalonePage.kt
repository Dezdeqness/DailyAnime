package com.dezdeqness.presentation.features.characterdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.details.character.presentation.CharacterDetailsPage
import com.dezdeqness.feature.details.character.presentation.CharacterDetailsViewModel
import com.dezdeqness.feature.details.character.presentation.CharacterIdKey
import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.AnimeDetails
import com.dezdeqness.presentation.PersonDetails
import kotlinx.coroutines.launch

@Composable
fun CharacterDetailsStandalonePage(
    modifier: Modifier = Modifier,
    characterId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val component = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .characterDetailsFeatureComponent()
            .create()
    }

    val owner = LocalViewModelStoreOwner.current
    val extras = if (owner is HasDefaultViewModelProviderFactory) {
        owner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }

    val viewModel = viewModel<CharacterDetailsViewModel>(
        key = "character_details_$characterId",
        factory = component.viewModelFactory(),
        extras = MutableCreationExtras(extras).apply { set(CharacterIdKey, characterId) },
    )

    val messageConsumer = remember { component.messageConsumer() }
    val messageProvider = remember { component.messageProvider() }
    val dispatcherProvider = remember { component.coroutineDispatcherProvider() }
    val scope = rememberCoroutineScope()
    val baseUrl = remember {
        (context.applicationContext as ShikimoriApp).appComponent.configManager.baseUrl.trimEnd('/')
    }

    CharacterDetailsPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        onUiEvent = viewModel::onUiEvent,
        onBackPressed = { navController.popBackStack() },
    )

    viewModel.effects.collectEvents { effect ->
        when (effect) {
            is CharacterDetailsNamespace.Effect.Base -> when (val base = effect.effect) {
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
            is CharacterDetailsNamespace.Effect.NavigateToAnime -> {
                navController.navigate(AnimeDetails(effect.animeId))
            }
            is CharacterDetailsNamespace.Effect.NavigateToPerson -> {
                navController.navigate(PersonDetails(effect.personId))
            }
        }
    }
}
