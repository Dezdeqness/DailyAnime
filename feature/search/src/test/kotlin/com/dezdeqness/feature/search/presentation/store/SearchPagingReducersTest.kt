package com.dezdeqness.feature.search.presentation.store

import app.cash.turbine.test
import com.dezdeqness.architecture.store.paging.PagingEvent
import com.dezdeqness.architecture.store.paging.PagingState
import com.dezdeqness.foundation.test.PluginStore
import com.dezdeqness.foundation.test.StoreTest
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPagingReducersTest : StoreTest() {

    private val pageLoaded = PagingEvent.PageLoaded<AnimeUiModel, SearchPageParams>(items = listOf(), nextParams = null)

    private val loadFailed = PagingEvent.LoadFailed<SearchError>(SearchError.Network)

    @Test
    fun `GIVEN scroll pending WHEN page loaded SHOULD emit ScrollToTop once`() = runTest(dispatcher) {
        val store = PluginStore(State(isScrollToTopPending = true), SearchPageLoadedReducer())

        store.effects.test {
            store.accept(pageLoaded)
            store.accept(pageLoaded)

            assertEquals(Effect.ScrollToTop, awaitItem())
            advanceUntilIdle()
            expectNoEvents()
        }
        assertEquals(false, store.state.isScrollToTopPending)
    }

    @Test
    fun `GIVEN no scroll pending WHEN page loaded SHOULD not scroll`() = runTest(dispatcher) {
        val store = PluginStore(State(), SearchPageLoadedReducer())

        store.effects.test {
            store.accept(pageLoaded)
            advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun `GIVEN items on screen WHEN load failed SHOULD show error and drop pending scroll`() = runTest(dispatcher) {
        val state = State(
            paging = PagingState(items = listOf(AnimeUiModel(id = 1L, title = "", kind = "", logoUrl = ""))),
            isScrollToTopPending = true,
        )
        val store = PluginStore(state, SearchLoadFailedReducer())

        store.effects.test {
            store.accept(loadFailed)

            assertEquals(Effect.ShowError, awaitItem())
        }
        assertEquals(false, store.state.isScrollToTopPending)
    }

    @Test
    fun `GIVEN empty list WHEN load failed SHOULD not show error message`() = runTest(dispatcher) {
        val store = PluginStore(State(), SearchLoadFailedReducer())

        store.effects.test {
            store.accept(loadFailed)
            advanceUntilIdle()

            expectNoEvents()
        }
    }
}
