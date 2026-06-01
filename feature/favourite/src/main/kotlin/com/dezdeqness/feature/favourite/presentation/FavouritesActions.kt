package com.dezdeqness.feature.favourite.presentation

import com.dezdeqness.contract.favourite.model.FavouriteType

interface FavouritesActions {
    fun onBackPressed()
    fun onItemClicked(id: Long, type: FavouriteType)
}
