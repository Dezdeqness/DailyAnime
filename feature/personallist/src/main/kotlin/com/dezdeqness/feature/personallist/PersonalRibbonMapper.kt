package com.dezdeqness.feature.personallist

import com.dezdeqness.contract.user.model.StatusEntity
import com.dezdeqness.core.provider.ResourceProvider
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel
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
