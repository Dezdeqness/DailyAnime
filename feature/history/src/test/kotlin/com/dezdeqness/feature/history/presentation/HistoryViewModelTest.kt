package com.dezdeqness.feature.history.presentation

import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Command
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Effect
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Event
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test

class HistoryViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<Event, State, Effect, Command>

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: BaseMessageProvider

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(State())
        every { store.effects } returns flowOf()

        every { messageProvider.getGeneralErrorMessage() } returns "Error occurred"

        coEvery { messageConsumer.onErrorMessage(any()) } returns Unit

        viewModel = HistoryViewModel(
            store = store,
            messageConsumer = messageConsumer,
            messageProvider = messageProvider,
            coroutineDispatcherProvider = object: CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Unconfined

                override fun io() = Dispatchers.Unconfined

                override fun computation() = Dispatchers.Unconfined
            },
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
