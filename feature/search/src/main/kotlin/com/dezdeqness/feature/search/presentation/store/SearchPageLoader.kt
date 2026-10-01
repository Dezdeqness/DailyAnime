package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.paging.PageLoader
import com.dezdeqness.architecture.store.paging.PageResult
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import com.dezdeqness.feature.search.presentation.AnimeUiMapper
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import java.io.IOException
import javax.inject.Inject

class SearchPageLoader @Inject constructor(
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val animeUiMapper: AnimeUiMapper,
) : PageLoader<AnimeUiModel, SearchPageParams, SearchError> {

    override suspend fun load(params: SearchPageParams): PageResult<AnimeUiModel, SearchPageParams, SearchError> =
        getAnimeListUseCase(pageNumber = params.page, params = params.params).fold(
            onSuccess = { state ->
                PageResult.Success(
                    items = animeUiMapper.map(state.list),
                    nextParams = if (state.hasNextPage) params.copy(page = state.currentPage) else null,
                )
            },
            onFailure = { throwable ->
                val error = when (throwable) {
                    is IOException -> SearchError.Network
                    else -> SearchError.Unknown(throwable)
                }
                PageResult.Failure(error)
            },
        )
}
