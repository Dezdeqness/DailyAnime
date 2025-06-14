package com.dezdeqness.presentation.features.screenshotsviewer

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test
import utils.TestCoroutineDispatcherProvider

class ScreenshotsViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<Event, State, Effect, Command>

    @MockK
    private lateinit var appLogger: AppLogger

    private lateinit var viewModel: ScreenshotsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(State(listOf(), 0))
        every { store.effects } returns flowOf()

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit

        viewModel = ScreenshotsViewModel(
            store = store,
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            appLogger = appLogger,
        )
    }

    @Test
    fun `WHEN onShareButtonClicked invoked SHOULD calls store with ShareUrlClicked`() {
        viewModel.onShareButtonClicked()

        verify { store.accept(Event.ShareUrlClicked) }
    }

    @Test
    fun `WHEN onScreenShotChanged invoked SHOULD calls store with IndexChanged`() {
        viewModel.onScreenShotChanged(3)

        verify { store.accept(Event.IndexChanged(3)) }
    }
}
