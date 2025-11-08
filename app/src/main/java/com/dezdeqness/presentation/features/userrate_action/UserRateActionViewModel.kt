package com.dezdeqness.presentation.features.userrate_action

import com.dezdeqness.contract.pinned.repository.PinnedUserRateRepository
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRateActionViewModel @Inject constructor(
    private val pinnedUserRateRepository: PinnedUserRateRepository,
    private val composer: UserRateActionsComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    override val viewModelTag = "UserRateActionViewModel"

    private val _userRateActionsFlow: MutableStateFlow<ImmutableList<UserRateActions>> =
        MutableStateFlow(ImmutableList.of())

    val userRateActionsFlow: StateFlow<ImmutableList<UserRateActions>> get() = _userRateActionsFlow

    private var userRateId: Long = 0

    fun onUserRateUpdated(rateId: Long) {
        launchOnIo {
            userRateId = rateId
            val isPinned = pinnedUserRateRepository.isPinned(rateId)
            composer.composeActions(isPinned = isPinned)
        }
    }

}
