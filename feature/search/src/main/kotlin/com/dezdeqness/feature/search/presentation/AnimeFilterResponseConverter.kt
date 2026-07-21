package com.dezdeqness.feature.search.presentation

import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import javax.inject.Inject

class AnimeFilterResponseConverter @Inject constructor() {

    fun convertSearchFilterToQueryMap(list: Collection<SearchSectionUiModel>): Map<String, String> =
        list
            .mapNotNull { filter ->
                val selectedIds = filter.items
                    .filter { it.id in filter.selectedCells }
                    .joinToString(separator = ",") { animeCell -> animeCell.id }

                if (selectedIds.isNotEmpty()) filter.queryId to selectedIds else null
            }
            .toMap()
}
