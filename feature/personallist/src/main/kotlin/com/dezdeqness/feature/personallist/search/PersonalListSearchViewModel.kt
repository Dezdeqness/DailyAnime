package com.dezdeqness.feature.personallist.search

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import com.dezdeqness.domain.usecases.SearchPersonalListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalListSearchViewModel @Inject constructor(
    private val searchPersonalListUseCase: SearchPersonalListUseCase,
    private val userRateUiMapper: SearchUserRateUiMapper,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "PersonalListSearchViewModel"

    private val events = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val results = events
        .onStart { emit(SearchEvent.Initial) }
        .flatMapLatest { event ->
            when (event) {
                is SearchEvent.Search -> flow {
                    emit(
                        SearchResult.Loading
                    )

                    val result = searchPersonalListUseCase(event.query)

                    emit(
                        SearchResult.Result(
                            query = event.query,
                            result = result
                        )
                    )
                }.flowOn(coroutineDispatcherProvider.io())

                else -> flow {
                    emit(SearchResult.Event(event))
                }
            }
        }

    val searchState: StateFlow<PersonalListSearchState> =
        results
            .scan(PersonalListSearchState()) { previous, result ->
                when (result) {

                    is SearchResult.Event -> when (val event = result.event) {
                        is SearchEvent.TabSelect -> previous.copy(selectedTab = event.tab)
                        is SearchEvent.Initial -> PersonalListSearchState()
                        else -> previous
                    }

                    is SearchResult.Loading -> {
                        previous.copy(status = PersonalListSearchStatus.Loading)
                    }

                    is SearchResult.Result -> {
                        if (result.result.isSuccess) {
                            val list = result.result.getOrThrow()
                            val mapped = userRateUiMapper.map(list)
                            PersonalListSearchState(
                                list = mapped,
                                status = if (mapped.isEmpty()) {
                                    PersonalListSearchStatus.Empty
                                } else {
                                    PersonalListSearchStatus.Loaded
                                },
                                query = result.query,
                            )
                        } else {
                            onErrorMessage()
                            logInfo(
                                "Error while personal search",
                                result.result.exceptionOrNull() ?: Exception(),
                            )
                            previous.copy(status = PersonalListSearchStatus.Error)
                        }
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PersonalListSearchState()
            )


    fun onQueryChanged(query: String) {
        if (searchState.value.query == query) return
        val event = if (query.isEmpty()) {
            SearchEvent.Initial
        } else {
            SearchEvent.Search(query)
        }
        events.tryEmit(event)
    }

    fun onTabSelected(tab: PersonalListTab) {
        if (searchState.value.selectedTab == tab) return
        events.tryEmit(SearchEvent.TabSelect(tab))
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
        data class TabSelect(val tab: PersonalListTab) : SearchEvent
        data class Search(val query: String) : SearchEvent
    }

    private sealed interface SearchResult {

        data class Event(
            val event: SearchEvent
        ) : SearchResult

        data object Loading : SearchResult

        data class Result(
            val query: String,
            val result: kotlin.Result<List<UserRateEntity>>
        ) : SearchResult
    }
}
