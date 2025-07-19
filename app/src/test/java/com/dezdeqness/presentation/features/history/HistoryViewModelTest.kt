package com.dezdeqness.presentation.features.history

import com.dezdeqness.core.MessageProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Effect
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Event
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.State
import com.dezdeqness.presentation.message.MessageConsumer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test
import utils.TestCoroutineDispatcherProvider

class HistoryViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<Event, State, Effect, Command>

    @MockK
    private lateinit var appLogger: AppLogger

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: MessageProvider

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(State())
        every { store.effects } returns flowOf()

        every { messageProvider.getGeneralErrorMessage() } returns "Error occurred"

        viewModel = HistoryViewModel(
            store = store,
            messageConsumer = messageConsumer,
            messageProvider = messageProvider,
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            appLogger = appLogger,
        )
    }

    @Test
    fun `WHEN onPullDownRefreshed invoked SHOULD trigger Refresh event`() {
        viewModel.onPullDownRefreshed()

        verify { store.accept(Event.Refresh) }
    }

    @Test
    fun `WHEN onLoadMore invoked SHOULD trigger LoadMore event`() {
        viewModel.onLoadMore()

        verify { store.accept(Event.LoadMore) }
    }

    @Test
    fun `WHEN onErrorMessage invoked SHOULD trigger show generic error`() {
        viewModel.onErrorMessage()
        coEvery {
            messageConsumer.onErrorMessage(any())
        }
    }
}
