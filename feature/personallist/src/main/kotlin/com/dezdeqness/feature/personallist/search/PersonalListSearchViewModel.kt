package com.dezdeqness.feature.personallist.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.usecases.SearchPersonalListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalListSearchViewModel @Inject constructor(
    private val searchPersonalListUseCase: SearchPersonalListUseCase,
    private val userRateUiMapper: UserRateUiMapper,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
) : ViewModel() {

    private val events = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 1)

    private val stateFlow = events
        .scan(PersonalListSearchState()) { state, event ->
            when (event) {
                is SearchEvent.Initial -> state

                is SearchEvent.QueryChanged -> {
                    state.copy(query = event.query)
                }

                is SearchEvent.TabSelect -> {
                    state.copy(selectedTab = event.tab)
                }

                is SearchEvent.Search -> {
                    state.copy(status = PersonalListSearchStatus.Loading)
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchState: StateFlow<PersonalListSearchState> =
        stateFlow
            .flatMapLatest { state ->
                val event = events.replayCache.lastOrNull()
                if (event !is SearchEvent.Search) {
                    flow { emit(state) }
                } else {
                    flow {
                        val result = searchPersonalListUseCase(
                            search = state.query
                        )

                        result.fold(
                            onSuccess = { list ->
                                val mapped = userRateUiMapper.map(list)
                                emit(
                                    state.copy(
                                        list = mapped,
                                        status = if (mapped.isEmpty())
                                            PersonalListSearchStatus.Empty
                                        else
                                            PersonalListSearchStatus.Loaded
                                    )
                                )
                            },
                            onFailure = { error ->
                                appLogger.logInfo(TAG, "Personal search failed", error)
                                emit(state.copy(status = PersonalListSearchStatus.Error))
                                onErrorMessage()
                            }
                        )

                    }.flowOn(coroutineDispatcherProvider.io())
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PersonalListSearchState()
            )

    fun onQueryChanged(query: String) {
        if (searchState.value.query == query) return
        events.tryEmit(SearchEvent.QueryChanged(query))
    }

    fun onTabSelected(tab: PersonalListTab) {
        if (searchState.value.selectedTab == tab) return
        events.tryEmit(SearchEvent.TabSelect(tab))
    }

    fun onSearch() {
        events.tryEmit(SearchEvent.Search)
    }

    private fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(
                messageProvider.getGeneralErrorMessage()
            )
        }
    }

    private sealed interface SearchEvent {
        data object Initial : SearchEvent
        data class QueryChanged(val query: String) : SearchEvent
        data class TabSelect(val tab: PersonalListTab) : SearchEvent
        data object Search : SearchEvent
    }

    companion object {
        private const val TAG = "PersonalListSearchViewModel"
    }
}
