package com.dezdeqness.feature.search.presentation

import com.dezdeqness.architecture.store.paging.PagingEvent
import com.dezdeqness.contract.filter.model.AnimeCell
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import money.vivid.elmslie.core.store.ElmStore
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val states = MutableStateFlow(State())
    private val effects = MutableSharedFlow<Effect>()
    private val store = mockk<ElmStore<Any, State, Effect, Any>>(relaxUnitFun = true)
    private val messageConsumer = mockk<MessageConsumer>(relaxed = true)
    private val messageProvider = mockk<BaseMessageProvider>()

    private lateinit var viewModel: AnimeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { store.states } returns states
        every { store.effects } returns effects
        every { messageProvider.getGeneralErrorMessage() } returns "error"

        viewModel = AnimeViewModel(store, messageConsumer, messageProvider)
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN created SHOULD send Init to the store`() = runTest(dispatcher) {
        verify(exactly = 1) { store.accept(Event.Init) }
    }

    @Test
    fun `WHEN query changed SHOULD send QueryChanged`() = runTest(dispatcher) {
        viewModel.onQueryChanged("naruto")

        verify { store.accept(Event.QueryChanged("naruto")) }
    }

    @Test
    fun `WHEN filters changed SHOULD send FiltersChanged`() = runTest(dispatcher) {
        val filters = listOf(section(selected = setOf("1")))

        viewModel.onFilterChanged(filters)

        verify { store.accept(Event.FiltersChanged(filters)) }
    }

    @Test
    fun `WHEN pulled to refresh SHOULD send PullRefreshed`() = runTest(dispatcher) {
        viewModel.onPullDownRefreshed()

        verify { store.accept(Event.PullRefreshed) }
    }

    @Test
    fun `WHEN load more requested SHOULD send paging LoadMore`() = runTest(dispatcher) {
        viewModel.onLoadMore()

        verify { store.accept(PagingEvent.LoadMore) }
    }

    @Test
    fun `WHEN filter button clicked SHOULD send FilterClicked`() = runTest(dispatcher) {
        viewModel.onFabClicked()

        verify { store.accept(Event.FilterClicked) }
    }

    private fun section(selected: Set<String>) = SearchSectionUiModel(
        innerId = "kind",
        queryId = "kind",
        displayName = "kind",
        items = listOf(AnimeCell(id = "1", displayName = "1")),
        selectedCells = selected,
    )
}
