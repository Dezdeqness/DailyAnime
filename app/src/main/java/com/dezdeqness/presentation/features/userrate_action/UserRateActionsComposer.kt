package com.dezdeqness.presentation.features.userrate_action

import com.dezdeqness.presentation.features.userrate_action.UserRateActions.PinAction
import com.dezdeqness.presentation.features.userrate_action.UserRateActions.UnpinAction
import com.google.common.collect.ImmutableList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRateActionsComposer @Inject constructor() {

    fun composeActions(isPinned: Boolean): ImmutableList<UserRateActions> {
        val action = if (isPinned) UnpinAction else PinAction
        return ImmutableList.of(action)
    }
}
