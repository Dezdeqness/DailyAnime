package com.dezdeqness.feature.topicdetails.presentation

import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import money.vivid.elmslie.core.store.ElmStore
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopicDetailsViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<TopicDetailsNamespace.Event, TopicDetailsNamespace.State, TopicDetailsNamespace.Effect, TopicDetailsNamespace.Command>

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: BaseMessageProvider

    private lateinit var viewModel: TopicDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(TopicDetailsNamespace.State())
        every { store.effects } returns flowOf()
        every { messageProvider.getGeneralErrorMessage() } returns "Error occurred"
        coEvery { messageConsumer.onErrorMessage(any()) } returns Unit

        viewModel = TopicDetailsViewModel(
            store = store,
            messageConsumer = messageConsumer,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            messageProvider = messageProvider,
            topicId = 42L,
        )
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN state is collected SHOULD trigger InitialLoad event`() = runTest {
        val job = launch { viewModel.state.collect { } }

        advanceUntilIdle()

        verify { store.accept(TopicDetailsNamespace.Event.InitialLoad(42L)) }
        job.cancel()
    }

    @Test
    fun `WHEN onPullDownRefreshed invoked SHOULD trigger Refresh event`() {
        viewModel.onPullDownRefreshed()

        verify { store.accept(TopicDetailsNamespace.Event.Refresh(42L)) }
    }

    @Test
    fun `WHEN onErrorMessage invoked SHOULD show generic error`() = runTest {
        viewModel.onErrorMessage()
        advanceUntilIdle()

        coVerify { messageConsumer.onErrorMessage("Error occurred") }
    }
}
