package com.dezdeqness.presentation.features.history.store

import app.cash.turbine.test
import com.dezdeqness.presentation.features.history.models.HistoryModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryReducerTest {

    @MockK(relaxed = true)
    private lateinit var historyActor: HistoryActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN InitialLoad AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = HistoryNamespace.State(),
            reducer = historyReducer,
            actor = historyActor,
        )

        val items = listOf(
            mockk<HistoryModel>(),
            mockk<HistoryModel>(),
            mockk<HistoryModel>(),
        )

        every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        HistoryNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(HistoryNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    status = HistoryStatus.Loading,
                    currentPage = 1,
                ),
                loadingState
            )

            val loadedState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    status = HistoryStatus.Loaded,
                    currentPage = 1,
                    hasNextPage = true,
                    list = items,
                ),
                loadedState
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN InitialLoad AND it succeeds BUT its empty SHOULD show Empty status`(): Unit =
        runTest {
            val store = ElmStore(
                initialState = HistoryNamespace.State(),
                reducer = historyReducer,
                actor = historyActor,
            )

            val items = listOf<HistoryModel>()

            every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            HistoryNamespace.Event.OnPageLoaded(
                                list = items,
                                hasNextPage = false
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(HistoryNamespace.Event.InitialLoad)

                val loadingState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        status = HistoryStatus.Loading,
                        currentPage = 1,
                    ),
                    loadingState
                )

                val loadedState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        status = HistoryStatus.Empty,
                        currentPage = 1,
                        hasNextPage = false,
                        list = items,
                    ),
                    loadedState
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN InitialLoad AND it fails SHOULD show Error status`(): Unit = runTest {
        val store = ElmStore(
            initialState = HistoryNamespace.State(),
            reducer = historyReducer,
            actor = historyActor,
        )

        every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(HistoryNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(HistoryNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    status = HistoryStatus.Loading,
                    currentPage = 1,
                ),
                loadingState
            )

            val loadedState = awaitItem()
            assertEquals(
                HistoryNamespace.State(status = HistoryStatus.Error),
                loadedState
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN LoadMore AND it succeeds SHOULD concatenate data with Loaded status`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
            )
            val store = ElmStore(
                initialState = HistoryNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = HistoryStatus.Loading,
                ),
                reducer = historyReducer,
                actor = historyActor,
            )

            val items = listOf(
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
            )

            every {
                historyActor.execute(
                    HistoryNamespace.Command.LoadPage(
                        page = 2,
                        isLoadMore = true
                    )
                )
            } returns
                    flow {
                        emit(
                            HistoryNamespace.Event.OnLoadMorePageLoaded(
                                list = items,
                                hasNextPage = true
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(HistoryNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        status = HistoryStatus.Loaded,
                        currentPage = 2,
                        hasNextPage = true,
                        list = initialItems + items,
                    ),
                    loadedState
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN LoadMore AND its empty SHOULD show existing data with hasNextPage false`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
            )
            val store = ElmStore(
                initialState = HistoryNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = HistoryStatus.Loading,
                ),
                reducer = historyReducer,
                actor = historyActor,
            )

            every {
                historyActor.execute(
                    HistoryNamespace.Command.LoadPage(
                        page = 2,
                        isLoadMore = true
                    )
                )
            } returns
                    flow {
                        emit(
                            HistoryNamespace.Event.OnLoadMorePageLoaded(
                                list = listOf(),
                                hasNextPage = false
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(HistoryNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        status = HistoryStatus.Loaded,
                        currentPage = 2,
                        hasNextPage = false,
                        list = initialItems,
                    ),
                    loadedState
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN LoadMore AND it failure SHOULD concatenate data with Loaded status`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
                mockk<HistoryModel>(),
            )
            val store = ElmStore(
                initialState = HistoryNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = HistoryStatus.Loading,
                ),
                reducer = historyReducer,
                actor = historyActor,
            )

            every {
                historyActor.execute(
                    HistoryNamespace.Command.LoadPage(
                        page = 2,
                        isLoadMore = true
                    )
                )
            } returns flow { emit(HistoryNamespace.Event.OnLoadMorePageError("", Throwable())) }

            store.effects.test {
                store.accept(HistoryNamespace.Event.LoadMore)

                val errorEffect = awaitItem()
                assertEquals(
                    HistoryNamespace.Effect.Error,
                    errorEffect
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN Refresh AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = HistoryNamespace.State(),
            reducer = historyReducer,
            actor = historyActor,
        )

        val items = listOf(
            mockk<HistoryModel>(),
            mockk<HistoryModel>(),
            mockk<HistoryModel>(),
        )

        every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        HistoryNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(HistoryNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState
            )

            val loadedState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    currentPage = 1,
                    hasNextPage = true,
                    isPullDownRefreshing = false,
                    list = items,
                    status = HistoryStatus.Loaded,
                ),
                loadedState
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh AND it succeeds BUT its empty SHOULD show Empty status`(): Unit =
        runTest {
            val store = ElmStore(
                initialState = HistoryNamespace.State(),
                reducer = historyReducer,
                actor = historyActor,
            )

            val items = listOf<HistoryModel>()

            every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            HistoryNamespace.Event.OnPageLoaded(
                                list = items,
                                hasNextPage = false
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(HistoryNamespace.Event.Refresh)

                val loadingState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        isPullDownRefreshing = true,
                        currentPage = 1,
                    ),
                    loadingState
                )

                val loadedState = awaitItem()
                assertEquals(
                    HistoryNamespace.State(
                        status = HistoryStatus.Empty,
                        currentPage = 1,
                        hasNextPage = false,
                        isPullDownRefreshing = false,
                        list = items,
                    ),
                    loadedState
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN Refresh AND it fails SHOULD show Error status`(): Unit = runTest {
        val store = ElmStore(
            initialState = HistoryNamespace.State(),
            reducer = historyReducer,
            actor = historyActor,
        )

        every { historyActor.execute(HistoryNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(HistoryNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(HistoryNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState
            )

            val loadedState = awaitItem()
            assertEquals(
                HistoryNamespace.State(
                    status = HistoryStatus.Error,
                    isPullDownRefreshing = false,
                ),
                loadedState
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
