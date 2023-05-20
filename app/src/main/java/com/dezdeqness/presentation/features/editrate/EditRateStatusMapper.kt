package com.dezdeqness.presentation.features.editrate

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.presentation.models.UserRateStatusUiModel
import javax.inject.Inject

class EditRateStatusMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun map(status: String): UserRateStatusUiModel {
        val statusId = UserRateStatusEntity.fromString(status).status
        val statusDisplayName = resourceProvider.getString("$PREFIX_USER_RATE_STATUS$statusId")
        return UserRateStatusUiModel(
            id = statusId,
            displayName = statusDisplayName,
        )
    }

    companion object {
        private const val PREFIX_USER_RATE_STATUS = "edit_rate_status_"
    }

}
