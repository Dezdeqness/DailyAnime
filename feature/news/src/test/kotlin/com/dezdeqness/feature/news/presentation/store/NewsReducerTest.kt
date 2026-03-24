package com.dezdeqness.feature.news.presentation.store

import app.cash.turbine.test
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.foundation.test.MainDispatcherExtension
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
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NewsReducerTest {

    @MockK(relaxed = true)
    private lateinit var newsActor: NewsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN InitialLoad AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = NewsNamespace.State(),
            reducer = newsReducer,
            actor = newsActor,
        )

        val items = listOf(
            mockk<NewsUiModel>(),
            mockk<NewsUiModel>(),
            mockk<NewsUiModel>(),
        )

        every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        NewsNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true,
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(NewsNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    status = NewsStatus.Loading,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    status = NewsStatus.Loaded,
                    currentPage = 1,
                    hasNextPage = true,
                    list = items,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN InitialLoad AND it succeeds BUT its empty SHOULD show Empty status`(): Unit =
        runTest {
            val store = ElmStore(
                initialState = NewsNamespace.State(),
                reducer = newsReducer,
                actor = newsActor,
            )

            every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            NewsNamespace.Event.OnPageLoaded(
                                list = emptyList(),
                                hasNextPage = false,
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(NewsNamespace.Event.InitialLoad)

                val loadingState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        status = NewsStatus.Loading,
                        currentPage = 1,
                    ),
                    loadingState,
                )

                val loadedState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        status = NewsStatus.Empty,
                        currentPage = 1,
                        hasNextPage = false,
                        list = emptyList(),
                    ),
                    loadedState,
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN InitialLoad AND it fails SHOULD show Error status`(): Unit = runTest {
        val store = ElmStore(
            initialState = NewsNamespace.State(),
            reducer = newsReducer,
            actor = newsActor,
        )

        every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(NewsNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(NewsNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    status = NewsStatus.Loading,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                NewsNamespace.State(status = NewsStatus.Error),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN LoadMore AND it succeeds SHOULD concatenate data with Loaded status`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<NewsUiModel> { every { id() } returns "1" },
                mockk<NewsUiModel> { every { id() } returns "2" },
                mockk<NewsUiModel> { every { id() } returns "3" },
            )
            val store = ElmStore(
                initialState = NewsNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = NewsStatus.Loaded,
                ),
                reducer = newsReducer,
                actor = newsActor,
            )

            val newItems = listOf(
                mockk<NewsUiModel> { every { id() } returns "4" },
                mockk<NewsUiModel> { every { id() } returns "5" },
            )

            every {
                newsActor.execute(NewsNamespace.Command.LoadPage(page = 2, isLoadMore = true))
            } returns flow {
                emit(
                    NewsNamespace.Event.OnLoadMorePageLoaded(
                        list = newItems,
                        hasNextPage = true,
                    )
                )
            }

            store.states.drop(1).test {
                store.accept(NewsNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        status = NewsStatus.Loaded,
                        currentPage = 2,
                        hasNextPage = true,
                        list = initialItems + newItems,
                    ),
                    loadedState,
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN LoadMore AND its empty SHOULD show existing data with hasNextPage false`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<NewsUiModel> { every { id() } returns "1" },
                mockk<NewsUiModel> { every { id() } returns "2" },
            )
            val store = ElmStore(
                initialState = NewsNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = NewsStatus.Loaded,
                ),
                reducer = newsReducer,
                actor = newsActor,
            )

            every {
                newsActor.execute(NewsNamespace.Command.LoadPage(page = 2, isLoadMore = true))
            } returns flow {
                emit(
                    NewsNamespace.Event.OnLoadMorePageLoaded(
                        list = emptyList(),
                        hasNextPage = false,
                    )
                )
            }

            store.states.drop(1).test {
                store.accept(NewsNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        status = NewsStatus.Loaded,
                        currentPage = 2,
                        hasNextPage = false,
                        list = initialItems,
                    ),
                    loadedState,
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN LoadMore AND it fails SHOULD emit Error effect`(): Unit = runTest {
        val initialItems = listOf(
            mockk<NewsUiModel> { every { id() } returns "1" },
        )
        val store = ElmStore(
            initialState = NewsNamespace.State(
                list = initialItems,
                currentPage = 1,
                status = NewsStatus.Loaded,
            ),
            reducer = newsReducer,
            actor = newsActor,
        )

        every {
            newsActor.execute(NewsNamespace.Command.LoadPage(page = 2, isLoadMore = true))
        } returns flow {
            emit(NewsNamespace.Event.OnLoadMorePageError("", Throwable()))
        }

        store.effects.test {
            store.accept(NewsNamespace.Event.LoadMore)

            val errorEffect = awaitItem()
            assertEquals(NewsNamespace.Effect.Error, errorEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = NewsNamespace.State(),
            reducer = newsReducer,
            actor = newsActor,
        )

        val items = listOf(
            mockk<NewsUiModel>(),
            mockk<NewsUiModel>(),
        )

        every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        NewsNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true,
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(NewsNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    currentPage = 1,
                    hasNextPage = true,
                    isPullDownRefreshing = false,
                    list = items,
                    status = NewsStatus.Loaded,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh AND it succeeds BUT its empty SHOULD show Empty status`(): Unit =
        runTest {
            val store = ElmStore(
                initialState = NewsNamespace.State(),
                reducer = newsReducer,
                actor = newsActor,
            )

            every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            NewsNamespace.Event.OnPageLoaded(
                                list = emptyList(),
                                hasNextPage = false,
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(NewsNamespace.Event.Refresh)

                val loadingState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        isPullDownRefreshing = true,
                        currentPage = 1,
                    ),
                    loadingState,
                )

                val loadedState = awaitItem()
                assertEquals(
                    NewsNamespace.State(
                        status = NewsStatus.Empty,
                        currentPage = 1,
                        hasNextPage = false,
                        isPullDownRefreshing = false,
                        list = emptyList(),
                    ),
                    loadedState,
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN Refresh AND it fails SHOULD show Error status`(): Unit = runTest {
        val store = ElmStore(
            initialState = NewsNamespace.State(),
            reducer = newsReducer,
            actor = newsActor,
        )

        every { newsActor.execute(NewsNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(NewsNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(NewsNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                NewsNamespace.State(
                    status = NewsStatus.Error,
                    isPullDownRefreshing = false,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
