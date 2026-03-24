package com.dezdeqness.feature.news.presentation

import com.dezdeqness.feature.news.presentation.store.NewsNamespace
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import money.vivid.elmslie.core.store.ElmStore
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<NewsNamespace.Event, NewsNamespace.State, NewsNamespace.Effect, NewsNamespace.Command>

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: BaseMessageProvider

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(NewsNamespace.State())
        every { store.effects } returns flowOf()

        every { messageProvider.getGeneralErrorMessage() } returns "Error occurred"

        coEvery { messageConsumer.onErrorMessage(any()) } returns Unit

        viewModel = NewsViewModel(
            store = store,
            messageConsumer = messageConsumer,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            messageProvider = messageProvider,
        )
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onPullDownRefreshed invoked SHOULD trigger Refresh event`() {
        viewModel.onPullDownRefreshed()

        verify { store.accept(NewsNamespace.Event.Refresh) }
    }

    @Test
    fun `WHEN onLoadMore invoked SHOULD trigger LoadMore event`() {
        viewModel.onLoadMore()

        verify { store.accept(NewsNamespace.Event.LoadMore) }
    }

    @Test
    fun `WHEN onErrorMessage invoked SHOULD trigger show generic error`() {
        viewModel.onErrorMessage()

        coEvery {
            messageConsumer.onErrorMessage(any())
        }
    }
}
