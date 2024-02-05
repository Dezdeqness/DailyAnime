package com.dezdeqness.presentation.features.screenshotsviewer

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.presentation.event.PagerScrollToPage
import com.dezdeqness.presentation.event.ShareUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

class ScreenshotsViewModel @Inject constructor(
    @Named("index") val index: Int,
    @Named("screenshots") val screenshots: List<String>,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag = "ScreenshotsViewModel"

    private val _screenshotState: MutableStateFlow<ScreenshotViewerState> =
        MutableStateFlow(ScreenshotViewerState())
    val screenshotState: StateFlow<ScreenshotViewerState> = _screenshotState

    init {
        _screenshotState.update {
            it.copy(
                screenshots = screenshots,
                currentIndex = index,
            )
        }

        onEventReceive(PagerScrollToPage(index))
    }

    fun onShareButtonClicked() {
        val state = _screenshotState.value
        val url = state.screenshots[state.currentIndex]
        onEventReceive(
            ShareUrl(
                url = url
            )
        )
    }

    fun onScreenShotChanged(index: Int) {
        _screenshotState.update {
            it.copy(
                currentIndex = index,
            )
        }
    }
}
