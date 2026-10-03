package com.dezdeqness.feature.search.presentation.store

import app.cash.turbine.test
import com.dezdeqness.architecture.store.paging.PagingCommand
import com.dezdeqness.architecture.store.paging.PagingState
import com.dezdeqness.foundation.test.PluginStore
import com.dezdeqness.foundation.test.StoreTest
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.filter.model.AnimeCell
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Command
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchReducerTest : StoreTest() {

    private val loadedState = State(
        paging = PagingState(items = listOf(animeItem()), nextParams = firstPage().copy(page = 2)),
        query = "naruto",
    )

    @Test
    fun `WHEN Init SHOULD start loading the first page and observe settings`() = runTest(dispatcher) {
        val store = PluginStore(State(), SearchReducer())

        store.commands.test {
            store.accept(Event.Init)

            assertEquals(PagingCommand.LoadPage(firstPage()), awaitItem())
            assertEquals(Command.ObserveAdultContent, awaitItem())
        }
        assertEquals(PagingState<AnimeUiModel, SearchPageParams, SearchError>(nextParams = firstPage(), isLoading = true), store.state.paging)
    }

    @Test
    fun `WHEN query changed SHOULD restart paging with the query and request scroll to top`() = runTest(dispatcher) {
        val store = PluginStore(loadedState, SearchReducer())
        val params = firstPage(text = "bleach")

        store.commands.test {
            store.accept(Event.QueryChanged("bleach"))

            assertEquals(PagingCommand.LoadPage(params), awaitItem())
        }
        assertEquals("bleach", store.state.query)
        assertEquals(loadedState.paging.restart(params), store.state.paging)
        assertEquals(true, store.state.isScrollToTopPending)
    }

    @Test
    fun `WHEN the same query is sent again SHOULD do nothing`() = runTest(dispatcher) {
        val store = PluginStore(loadedState, SearchReducer())

        store.commands.test {
            store.accept(Event.QueryChanged("naruto"))
            advanceUntilIdle()

            expectNoEvents()
        }
        assertEquals(loadedState, store.state)
    }

    @Test
    fun `WHEN filters changed SHOULD merge sections sharing a query id into one filter`() = runTest(dispatcher) {
        val store = PluginStore(loadedState, SearchReducer())
        val filters = listOf(
            section(innerId = "genre", queryId = "genre", selected = setOf("1")),
            section(innerId = "theme", queryId = "genre", selected = setOf("2")),
            section(innerId = "kind", queryId = "kind", selected = setOf()),
        )

        store.commands.test {
            store.accept(Event.FiltersChanged(filters))

            val params = firstPage(text = "naruto", filters = mapOf("genre" to setOf("1", "2")))
            assertEquals(PagingCommand.LoadPage(params), awaitItem())
        }
        assertEquals(filters, store.state.filters)
        assertEquals(true, store.state.isScrollToTopPending)
    }

    @Test
    fun `WHEN pull refreshed SHOULD restart paging with current params without scrolling`() = runTest(dispatcher) {
        val store = PluginStore(loadedState, SearchReducer())
        val params = firstPage(text = "naruto")

        store.commands.test {
            store.accept(Event.PullRefreshed)

            assertEquals(PagingCommand.LoadPage(params), awaitItem())
        }
        assertEquals(loadedState.paging.restart(params), store.state.paging)
        assertEquals(false, store.state.isScrollToTopPending)
    }

    @Test
    fun `WHEN adult content setting changed SHOULD reload with current params`() = runTest(dispatcher) {
        val store = PluginStore(loadedState, SearchReducer())

        store.commands.test {
            store.accept(Event.AdultContentChanged)

            assertEquals(PagingCommand.LoadPage(firstPage(text = "naruto")), awaitItem())
        }
    }

    @Test
    fun `WHEN filter clicked SHOULD emit OpenFilters with current filters`() = runTest(dispatcher) {
        val filters = listOf(section(innerId = "kind", queryId = "kind", selected = setOf("1")))
        val store = PluginStore(loadedState.copy(filters = filters), SearchReducer())

        store.effects.test {
            store.accept(Event.FilterClicked)

            assertEquals(Effect.OpenFilters(filters), awaitItem())
        }
    }

    private fun firstPage(text: String = "", filters: Map<String, Set<String>> = emptyMap()) =
        SearchPageParams(page = 1, params = AnimeSearchParams(text = text, filters = filters))

    private fun section(innerId: String, queryId: String, selected: Set<String>) = SearchSectionUiModel(
        innerId = innerId,
        queryId = queryId,
        displayName = innerId,
        items = listOf(AnimeCell(id = "1", displayName = "1"), AnimeCell(id = "2", displayName = "2")),
        selectedCells = selected,
    )

    private fun animeItem() = AnimeUiModel(id = 1L, title = "Naruto", kind = "TV", logoUrl = "")
}
