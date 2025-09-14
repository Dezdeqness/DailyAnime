package com.dezdeqness.core

import com.dezdeqness.R
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.data.provider.ResourceProvider
import javax.inject.Inject

class MessageProvider @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : BaseMessageProvider {

    override fun getAnimeEditCreateSuccessMessage() =
        resourceProvider.getString(R.string.edit_rate_create_success)

    override fun getAnimeEditUpdateSuccessMessage() =
        resourceProvider.getString(R.string.edit_rate_update_success)

    override fun getAnimeEditRateErrorMessage() =
        resourceProvider.getString(R.string.edit_rate_error)

    override fun getGeneralErrorMessage() =
        resourceProvider.getString(R.string.general_error)

}
