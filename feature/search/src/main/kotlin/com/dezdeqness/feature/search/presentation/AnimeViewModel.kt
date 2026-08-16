package com.dezdeqness.feature.search.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.contract.history.repository.HistorySearchRepository
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AnimeViewModel @Inject constructor(
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val animeUiMapper: AnimeUiMapper,
    private val animeFilterResponseConverter: AnimeFilterResponseConverter,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val historySearchRepository: HistorySearchRepository,
    private val settingsRepository: SettingsRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "SearchListViewModel"

    private val loadEvents = MutableSharedFlow<LoadEvent>(extraBufferCapacity = 1)

    private val _pullRefreshFlow = MutableStateFlow(false)
    val pullRefreshFlow: StateFlow<Boolean> get() = _pullRefreshFlow

    private val _scrollNeedFlow = MutableStateFlow(false)
    val scrollNeedFlow: StateFlow<Boolean> get() = _scrollNeedFlow

    private val _isListScrolling = MutableStateFlow(false)
    val isListScrolling: StateFlow<Boolean> get() = _isListScrolling

    private val navigateToFilterChannel = Channel<List<SearchSectionUiModel>>(Channel.BUFFERED)
    val navigateToFilter: Flow<List<SearchSectionUiModel>> = navigateToFilterChannel.receiveAsFlow()

    val historySearchFlow = historySearchRepository.getSearchHistoryFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = listOf(),
    )

    init {
        launchOnIo {
            settingsRepository
                .observePreference(AdultContentPreference)
                .drop(1)
                .distinctUntilChanged()
                .map {
                    val input = animeSearchState.value.input
                    loadEvents.tryEmit(LoadEvent.Refresh(input))
                }
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                )
                .collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val animeSearchState: StateFlow<AnimeSearchState> = loadEvents
        .onStart { emit(LoadEvent.Initial()) }
        .flatMapLatest { event ->
            flow {
                val result = getAnimeListUseCase.invoke(
                    pageNumber = event.page,
                    searchQuery = event.input.query,
                    queryMap = animeFilterResponseConverter.convertSearchFilterToQueryMap(event.input.filters),
                )

                emit(LoadResult(event = event, result = result))
            }.flowOn(coroutineDispatcherProvider.io())
        }
        .scan(AnimeSearchState()) { previous, loadResult ->
            reduce(previous, loadResult)
        }
        .onEach { _pullRefreshFlow.tryEmit(false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = AnimeSearchState(),
        )

    fun onScrolled() {
        if (_scrollNeedFlow.value) {
            _scrollNeedFlow.update { false }
        }
    }

    fun onPullDownRefreshed() {
        val input = animeSearchState.value.input
        loadEvents.tryEmit(LoadEvent.Refresh(input))
        _pullRefreshFlow.tryEmit(true)
    }

    fun onFabClicked() {
        navigateToFilterChannel.trySend(animeSearchState.value.input.filters)
    }

    fun onFilterChanged(filtersList: List<SearchSectionUiModel>) {
        val input = animeSearchState.value.input.copy(filters = filtersList)
        loadEvents.tryEmit(LoadEvent.Refresh(input, isScrollNeed = true))
        _pullRefreshFlow.tryEmit(true)
    }

    fun onQueryChanged(query: String) {
        if (animeSearchState.value.input.query == query) return
        val input = animeSearchState.value.input.copy(query = query)
        loadEvents.tryEmit(LoadEvent.Refresh(input, isScrollNeed = true))
        _pullRefreshFlow.tryEmit(true)
        launchOnIo {
            historySearchRepository.addSearchHistory(query)
        }
    }

    fun onLoadMore() {
        val state = animeSearchState.value
        if (state.hasNextPage) {
            loadEvents.tryEmit(LoadEvent.LoadMore(state.input, state.currentPage))
        }
    }

    fun onScrollInProgress(isScrollInProgression: Boolean) {
        _isListScrolling.value = isScrollInProgression
    }

    fun onRemoveSearchHistoryItem(item: String) {
        launchOnIo {
            historySearchRepository.removeSearchHistory(item)
        }
    }

    private fun reduce(previous: AnimeSearchState, loadResult: LoadResult): AnimeSearchState {
        val event = loadResult.event
        val result = loadResult.result

        result.onSuccess { response ->
            val mappedList = animeUiMapper.map(response.list)
            val updatedList = when (event) {
                is LoadEvent.Refresh, is LoadEvent.Initial -> mappedList
                is LoadEvent.LoadMore -> previous.list + mappedList
            }

            if (event.isScrollNeed) {
                _scrollNeedFlow.update { true }
            }

            val newStatus = if (mappedList.isEmpty()) {
                AnimeSearchStatus.Empty
            } else {
                AnimeSearchStatus.Loaded
            }

            return previous.copy(
                list = updatedList,
                input = event.input,
                status = newStatus,
                currentPage = response.currentPage,
                hasNextPage = response.hasNextPage,
            )
        }

        result.onFailure { error ->
            logInfo(event.toString(), error)

            val newStatus = when (event) {
                is LoadEvent.Initial -> AnimeSearchStatus.Error
                else -> previous.status
            }

            if (event is LoadEvent.LoadMore) {
                onErrorMessage()
            }

            _scrollNeedFlow.update { false }

            return previous.copy(status = newStatus)
        }

        return previous
    }

    private fun onErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }

    private sealed class LoadEvent(
        open val input: AnimeUserInput,
        open val page: Int,
        open val isScrollNeed: Boolean,
    ) {
        data class Refresh(
            override val input: AnimeUserInput,
            override val isScrollNeed: Boolean = false,
        ) : LoadEvent(input = input, page = INITIAL_PAGE, isScrollNeed = isScrollNeed)

        data class LoadMore(override val input: AnimeUserInput, override val page: Int) :
            LoadEvent(input = input, page = page, isScrollNeed = false)

        data class Initial(override val input: AnimeUserInput = AnimeUserInput()) :
            LoadEvent(input = input, page = INITIAL_PAGE, isScrollNeed = false)
    }

    private data class LoadResult(
        val event: LoadEvent,
        val result: Result<GetAnimeListUseCase.AnimeListState>,
    )

    companion object {
        private const val INITIAL_PAGE = 1
    }
}
