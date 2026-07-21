package com.dezdeqness.feature.search.presentation.preview

import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.AnimeSearchActions
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel

object AnimeSearchPreviewData {

    val list = listOf(
        AnimeUiModel(id = 1L, title = "Восстание Лелуша", kind = "TV", logoUrl = ""),
        AnimeUiModel(id = 2L, title = "Стальной алхимик", kind = "TV", logoUrl = ""),
        AnimeUiModel(id = 3L, title = "Атака титанов", kind = "TV", logoUrl = ""),
        AnimeUiModel(id = 4L, title = "Наруто", kind = "TV", logoUrl = ""),
        AnimeUiModel(id = 5L, title = "Ван-Пис", kind = "TV", logoUrl = ""),
        AnimeUiModel(id = 6L, title = "Блич", kind = "TV", logoUrl = ""),
    )

    val emptyActions = object : AnimeSearchActions {
        override fun onPullDownRefreshed() = Unit
        override fun onLoadMore() = Unit
        override fun onScrolled() = Unit
        override fun onAnimeClicked(animeId: Long, title: String) = Unit
        override fun onFabClicked() = Unit
        override fun onQueryChanged(query: String) = Unit
        override fun onFilterChanged(filtersList: List<SearchSectionUiModel>) = Unit
        override fun onScrollInProgress(isScrollInProgress: Boolean) = Unit
        override fun removeSearchHistoryItem(item: String) = Unit
    }
}
