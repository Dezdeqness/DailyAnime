package com.dezdeqness.feature.details.related.presentation

import com.dezdeqness.contract.anime.model.Entity
import com.dezdeqness.feature.details.related.presentation.models.RelatedListItem

interface RelatedListUiMapper {

    fun map(item: Entity): RelatedListItem?
}
