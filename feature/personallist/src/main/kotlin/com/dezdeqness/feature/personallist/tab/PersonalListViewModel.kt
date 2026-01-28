package com.dezdeqness.feature.personallist.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.core.di.AssistedViewModelFactory
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.feature.personallist.tab.store.PersonalListNamespace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

object StatusIdKey : CreationExtras.Key<String>

class PersonalListViewModel(
    private val store: ElmStore<PersonalListNamespace.Event, PersonalListNamespace.State, PersonalListNamespace.Effect, PersonalListNamespace.Command>,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val statusId: String,
) : ViewModel() {

    val state = store
        .states
        .onStart {
            store.accept(PersonalListNamespace.Event.CheckPendingRefresh)
            if (!store.states.value.hasPendingRefresh) {
                store.accept(PersonalListNamespace.Event.InitialLoad(status = statusId))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PersonalListNamespace.State()
        )

    fun onLoadMore() {
        store.accept(PersonalListNamespace.Event.LoadMore)
    }

    fun onRefresh() {
        store.accept(PersonalListNamespace.Event.Refresh)
    }

    fun removeItemLocally(userRateId: Long) {
        store.accept(PersonalListNamespace.Event.ItemRemovedLocally(userRateId))
    }

    fun dismissRefreshHint() {
        store.accept(PersonalListNamespace.Event.DismissRefreshHint)
    }

    fun markPendingRefresh() {
        store.accept(PersonalListNamespace.Event.MarkPendingRefresh)
    }

    class Factory @Inject constructor(
        private val messageConsumer: MessageConsumer,
        private val messageProvider: BaseMessageProvider,
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
            )
        }
    }
}
