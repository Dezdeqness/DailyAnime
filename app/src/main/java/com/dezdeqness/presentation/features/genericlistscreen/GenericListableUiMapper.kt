package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.contract.anime.model.Entity
import com.dezdeqness.presentation.models.AdapterItem

interface GenericListableUiMapper {

    fun map(item: Entity): AdapterItem?

}
