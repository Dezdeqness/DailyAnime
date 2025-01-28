package com.dezdeqness.presentation

import com.dezdeqness.presentation.models.SearchSectionUiModel
import javax.inject.Inject

class AnimeFilterResponseConverter @Inject constructor(){

    fun convertSearchFilterToQueryMap(list: Collection<SearchSectionUiModel>): Map<String, String> {
        return list
            .mapNotNull { filter ->
                val selectedIds = filter.items
                    .filter { it.id in filter.selectedCells }
                    .joinToString(separator = ",") { animeCell ->
                        // Currently disabled
                        if (false /*animeCell.state == CellState.EXCLUDE*/) {
                            "!${animeCell.id}"
                        } else {
                            animeCell.id
                        }
                    }

                // Add to map only if there are selected IDs
                if (selectedIds.isNotEmpty()) filter.innerId to selectedIds else null
            }
            .toMap() // Convert the resulting list to a map
    }

}
