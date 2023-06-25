package com.dezdeqness.core

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import javax.inject.Inject

class MessageProvider @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun getAnimeEditCreateSuccessMessage() =
        resourceProvider.getString(R.string.edit_rate_create_success)

    fun getAnimeEditUpdateSuccessMessage() =
        resourceProvider.getString(R.string.edit_rate_update_success)

    fun getAnimeEditRateErrorMessage() =
        resourceProvider.getString(R.string.edit_rate_error)

}