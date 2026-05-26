package com.dezdeqness.presentation.features.persondetails

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
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.person.presentation.PersonDetailsPage
import com.dezdeqness.feature.details.person.presentation.PersonDetailsViewModel
import com.dezdeqness.feature.details.person.presentation.PersonIdKey
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace
import com.dezdeqness.foundation.utils.collectEvents
import kotlinx.coroutines.launch

@Composable
fun PersonDetailsStandalonePage(
    modifier: Modifier = Modifier,
    personId: Long,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val component = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .personDetailsFeatureComponent()
            .create()
    }

    val owner = LocalViewModelStoreOwner.current
    val extras = if (owner is HasDefaultViewModelProviderFactory) {
        owner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }

    val viewModel = viewModel<PersonDetailsViewModel>(
        key = "person_details_$personId",
        factory = component.viewModelFactory(),
        extras = MutableCreationExtras(extras).apply { set(PersonIdKey, personId) },
    )

    val messageConsumer = remember { component.messageConsumer() }
    val messageProvider = remember { component.messageProvider() }
    val dispatcherProvider = remember { component.coroutineDispatcherProvider() }
    val scope = rememberCoroutineScope()
    val baseUrl = remember {
        (context.applicationContext as ShikimoriApp).appComponent.configManager.baseUrl.trimEnd('/')
    }

    PersonDetailsPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        onUiEvent = viewModel::onUiEvent,
        onBackPressed = { navController.popBackStack() },
    )

    viewModel.effects.collectEvents { effect ->
        when (effect) {
            is PersonDetailsNamespace.Effect.Base -> when (val base = effect.effect) {
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
            }
        }
    }
}
