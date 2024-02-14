package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.presentation.AnimeFilterResponseConverter
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.NavigateToFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.message.MessageConsumer
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.presentation.models.CellState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val animeUiMapper: AnimeUiMapper,
    private val animeFilterResponseConverter: AnimeFilterResponseConverter,
    private val actionConsumer: ActionConsumer,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable, BaseViewModel.LoadMore {

    private val _animeStateFlow: MutableStateFlow<AnimeState> = MutableStateFlow(AnimeState())
    val animeStateFlow: StateFlow<AnimeState> get() = _animeStateFlow

    private var filtersList: List<AnimeSearchFilter> = emptyList()

    private var query: String = ""

    private var currentPage = INITIAL_PAGE

    init {
        actionConsumer.attachListener(this)
        initialPageLoad()
    }

    override val viewModelTag = "SearchListViewModel"

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = {
                getAnimeListUseCase.invoke(
                    pageNumber = INITIAL_PAGE,
                    queryMap = animeFilterResponseConverter.convertSearchFilterToQueryMap(
                        filterSelectedCells()
                    ),
                    searchQuery = query,
                )
            },
            onSuccess = { state ->
                currentPage = state.currentPage
                _animeStateFlow.value = _animeStateFlow.value.copy(
                    list = animeUiMapper.map(state.list),
                    hasNextPage = state.hasNextPage,
                    isErrorStateShowing = false,
                )
            }
        )
    }

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _animeStateFlow.value = _animeStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _animeStateFlow.value = _animeStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun setLoadMoreIndicator(isVisible: Boolean) {
        // TODO:
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onScrollNeed() {
        if (animeStateFlow.value.isScrollNeed) {
            _animeStateFlow.update {
                _animeStateFlow.value.copy(isScrollNeed = false)
            }
            onEventReceive(ScrollToTop)
        }
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onFabClicked() {
        onEventReceive(NavigateToFilter(filters = filtersList))
    }

    fun applyFilter(filtersList: List<AnimeSearchFilter>) {
        this.filtersList = filtersList
        initialPageLoad(isScrollNeed = true)
    }

    fun onQueryChanged(query: String) {
        this.query = query
        initialPageLoad(isScrollNeed = true)
    }

    fun onQueryEmpty() {
        this.query = ""
        initialPageLoad()
    }

    fun onLoadMore() {
        onLoadMore(
            action = {
                getAnimeListUseCase.invoke(
                    pageNumber = currentPage,
                    queryMap = animeFilterResponseConverter.convertSearchFilterToQueryMap(
                        filterSelectedCells()
                    ),
                    searchQuery = query,
                )
            },
            onSuccess = { state ->
                val hasNextPage = state.hasNextPage
                currentPage = state.currentPage
                val list = animeUiMapper.map(state.list)

                _animeStateFlow.value = _animeStateFlow.value.copy(
                    list = _animeStateFlow.value.list + list,
                    hasNextPage = hasNextPage,
                )
                hasNextPage
            },
            onFailure = {
                onErrorMessage()
            }
        )
    }

    private fun initialPageLoad(isScrollNeed: Boolean = false) {
        onInitialLoad(
            action = {
                getAnimeListUseCase.invoke(
                    pageNumber = INITIAL_PAGE,
                    queryMap = animeFilterResponseConverter.convertSearchFilterToQueryMap(
                        filterSelectedCells()
                    ),
                    searchQuery = query,
                )
            },
            onSuccess = { state ->
                currentPage = state.currentPage
                val list = animeUiMapper.map(state.list)

                _animeStateFlow.value = _animeStateFlow.value.copy(
                    list = list,
                    hasNextPage = state.hasNextPage,
                    isEmptyStateShowing = list.isEmpty(),
                    isErrorStateShowing = false,
                    isScrollNeed = isScrollNeed,
                )
            },
            onFailure = {
                if (_animeStateFlow.value.list.isNotEmpty()) {
                    onErrorMessage()
                } else {
                    _animeStateFlow.value = _animeStateFlow.value.copy(
                        isErrorStateShowing = true,
                        isScrollNeed = false,
                    )
                }
            }
        )
    }

    private fun onErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }

    private fun filterSelectedCells() =
        filtersList.map { it.copy(items = it.items.filterNot { cellState -> cellState.state == CellState.NONE }) }
            .filter { it.items.isNotEmpty() }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
