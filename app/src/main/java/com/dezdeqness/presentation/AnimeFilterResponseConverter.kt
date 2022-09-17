package com.dezdeqness.presentation

import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.presentation.models.CellState
import javax.inject.Inject

class AnimeFilterResponseConverter @Inject constructor(){

    fun convertSearchFilterToQueryMap(list: Collection<AnimeSearchFilter>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        list.forEach { filter ->
            val stringBuilder = StringBuilder()
            filter.items.forEachIndexed { index, animeCell ->
                val value = if (animeCell.state == CellState.EXCLUDE) {
                    "!${animeCell.id}"
                } else {
                    animeCell.id
                }
                stringBuilder.append(value)
                if (filter.items.size - 1 != index) {
                    stringBuilder.append(",")
                }
            }
            map[filter.innerId] = stringBuilder.toString()

        }


        return map
    }

}
