package com.dezdeqness.presentation.features.personallist.userrate

import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger

class PersonalListUserRateViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.Refreshable, BaseViewModel.InitialLoaded {

    override val viewModelTag: String = "PersonalListUserRateViewModel"

    override fun onPullDownRefreshed() {

    }

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        TODO("Not yet implemented")
    }

}