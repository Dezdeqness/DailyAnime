package com.dezdeqness.feature.personallist.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.di.AssistedViewModelFactory
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import com.dezdeqness.feature.userrate.EditRateUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

object StatusIdKey : CreationExtras.Key<String>

class PersonalListViewModel(
    private val store: ElmStore<PersonalListNamespace.Event, PersonalListNamespace.State, PersonalListNamespace.Effect, PersonalListNamespace.Command>,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val statusId: String,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val state = store
        .states
        .onStart {
            store.accept(PersonalListNamespace.Event.InitialLoad(status = statusId))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PersonalListNamespace.State()
        )

    init {
        store
            .effects
            .onEach { effect ->
                when (effect) {
                    PersonalListNamespace.Effect.Error -> onEditErrorMessage()
                    PersonalListNamespace.Effect.EditUserRateSuccess -> onEditSuccessMessage()
                }
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
            )
            .launchIn(viewModelScope)
    }

    fun onLoadMore() {
        store.accept(PersonalListNamespace.Event.LoadMore)
    }

    fun onRefresh() {
        store.accept(PersonalListNamespace.Event.Refresh)
    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        store.accept(PersonalListNamespace.Event.UserRateChanged(userRate, statusId))
    }

    fun onUserRateIncrement(userRateId: Long) {
        store.accept(PersonalListNamespace.Event.UserRateIncrement(userRateId, statusId))
    }

    private fun onEditErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
        }
    }

    private fun onEditSuccessMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
        }
    }

    class Factory @Inject constructor(
        private val messageConsumer: MessageConsumer,
        private val messageProvider: BaseMessageProvider,
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
        private val store: ElmStore<
                PersonalListNamespace.Event,
                PersonalListNamespace.State,
                PersonalListNamespace.Effect,
                PersonalListNamespace.Command>
    ) : AssistedViewModelFactory<PersonalListViewModel> {

        override fun create(extras: CreationExtras): PersonalListViewModel {
            val statusId = extras[StatusIdKey].orEmpty()

            return PersonalListViewModel(
                statusId = statusId,
                messageConsumer = messageConsumer,
                messageProvider = messageProvider,
                store = store,
                coroutineDispatcherProvider = coroutineDispatcherProvider,
            )
        }
    }
}
