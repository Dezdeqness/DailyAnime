package com.dezdeqness.presentation.features.userrate_action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dezdeqness.R

sealed class UserRateActions(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
) {
    data object PinAction : UserRateActions(
        title = R.string.userrate_action_pin,
        icon = R.drawable.pin
    )

    data object UnpinAction : UserRateActions(
        title = R.string.userrate_action_unpin,
        icon = R.drawable.unpin
    )
}
