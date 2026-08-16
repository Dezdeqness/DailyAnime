package com.dezdeqness.feature.details.related.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.anime.usecases.BaseListableUseCase
import com.dezdeqness.feature.details.related.presentation.models.RelatedListItem
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class RelatedListViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    private val mapper: RelatedListUiMapper,
    private val baseListableUseCase: BaseListableUseCase,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "RelatedListViewModel"

    private val retryEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val stateFlow: StateFlow<RelatedListState> =
        retryEvents
            .onStart { emit(Unit) }
            .flatMapLatest { loadPage() }
            .flowOn(coroutineDispatcherProvider.io())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = RelatedListState(status = RelatedListStatus.Initial),
            )

    fun onRetryClicked() {
        retryEvents.tryEmit(Unit)
    }

    private fun loadPage() =
        flow {
            emit(RelatedListState(status = RelatedListStatus.Loading))

            baseListableUseCase.invoke(id = animeId)
                .onSuccess { entities ->
                    val list = entities.mapNotNull(mapper::map)
                    emit(
                        RelatedListState(
                            list = list,
                            status = if (list.isEmpty()) {
                                RelatedListStatus.Empty
                            } else {
                                RelatedListStatus.Loaded
                            },
                        ),
                    )
                }
                .onFailure {
                    logInfo("Error during initial loading of related list", it)
                    emit(RelatedListState(status = RelatedListStatus.Error))
                }
        }
            .catch {
                logInfo("Error in related list flow", it)
                emit(RelatedListState(status = RelatedListStatus.Error))
            }
}

data class RelatedListState(
    val list: List<RelatedListItem> = listOf(),
    val status: RelatedListStatus = RelatedListStatus.Initial,
)

enum class RelatedListStatus {
    Initial,
    Loading,
    Error,
    Empty,
    Loaded,
}
