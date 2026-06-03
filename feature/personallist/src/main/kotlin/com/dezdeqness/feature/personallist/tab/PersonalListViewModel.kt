package com.dezdeqness.feature.personallist.tab

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import com.dezdeqness.feature.userrate.EditRateUiModel
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore

object StatusIdKey : CreationExtras.Key<String>

class PersonalListViewModel(
    store: ElmStore<
        PersonalListNamespace.Event,
        PersonalListNamespace.State,
        PersonalListNamespace.Effect,
        PersonalListNamespace.Command,
        >,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val statusId: String,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : BaseStoreViewModel<
    PersonalListNamespace.Event,
    PersonalListNamespace.State,
    PersonalListNamespace.Effect,
    PersonalListNamespace.Command,
    >(
    store = store,
    initialState = PersonalListNamespace.State(),
    initialEvent = PersonalListNamespace.Event.InitialLoad(status = statusId),
) {

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
        accept(PersonalListNamespace.Event.LoadMore)
    }

    fun onRefresh() {
        accept(PersonalListNamespace.Event.Refresh)
    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        accept(PersonalListNamespace.Event.UserRateChanged(userRate, statusId))
    }

    fun onUserRateIncrement(userRateId: Long) {
        accept(PersonalListNamespace.Event.UserRateIncrement(userRateId, statusId))
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
            PersonalListNamespace.Command,
            >,
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
