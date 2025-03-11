package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.StatusEntity
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import javax.inject.Inject

class PersonalRibbonMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun map(item: StatusEntity) =
        RibbonStatusUiModel(
            id = item.groupedId,
            displayName = resourceProvider.getString(PREFIX_RIBBON_STATUS + item.name),
        )

    companion object {
        private const val PREFIX_RIBBON_STATUS = "anime_personal_list_ribbon_status_"
    }
}
