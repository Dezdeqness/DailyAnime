package com.dezdeqness.data.model.requet

import com.squareup.moshi.Json

data class FavouriteReorderRequest(
    @field:Json("new_index")
    val newIndex: Int
)
