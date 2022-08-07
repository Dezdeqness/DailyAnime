package com.dezdeqness.data.model

import com.dezdeqness.domain.model.FilterType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


data class FilterItem(
    val id: String,
    val name: String,
    val type: String,
)

class FilterTypeAdapter {

    @ToJson
    fun toJson(value: FilterType) = value.filterName

    @FromJson
    fun fromJson(value: String) = FilterType.fromString(value)

}
