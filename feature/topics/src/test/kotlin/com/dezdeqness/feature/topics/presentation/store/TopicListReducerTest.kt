package com.dezdeqness.feature.topics.presentation.store

import app.cash.turbine.test
import com.dezdeqness.feature.topics.presentation.models.TopicListUiModel
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
class TopicListReducerTest {

    @MockK(relaxed = true)
    private lateinit var topicListActor: TopicListActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN InitialLoad AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = TopicListNamespace.State(),
            reducer = topicListReducer,
            actor = topicListActor,
        )

        val items = listOf(
            mockk<TopicListUiModel>(),
            mockk<TopicListUiModel>(),
            mockk<TopicListUiModel>(),
        )

        every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        TopicListNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true,
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(TopicListNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    status = TopicListStatus.Loading,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    status = TopicListStatus.Loaded,
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
                initialState = TopicListNamespace.State(),
                reducer = topicListReducer,
                actor = topicListActor,
            )

            every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            TopicListNamespace.Event.OnPageLoaded(
                                list = emptyList(),
                                hasNextPage = false,
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(TopicListNamespace.Event.InitialLoad)

                val loadingState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        status = TopicListStatus.Loading,
                        currentPage = 1,
                    ),
                    loadingState,
                )

                val loadedState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        status = TopicListStatus.Empty,
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
            initialState = TopicListNamespace.State(),
            reducer = topicListReducer,
            actor = topicListActor,
        )

        every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(TopicListNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(TopicListNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    status = TopicListStatus.Loading,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                TopicListNamespace.State(status = TopicListStatus.Error),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN LoadMore AND it succeeds SHOULD concatenate data with Loaded status`(): Unit =
        runTest {
            val initialItems = listOf(
                mockk<TopicListUiModel> { every { id() } returns "1" },
                mockk<TopicListUiModel> { every { id() } returns "2" },
                mockk<TopicListUiModel> { every { id() } returns "3" },
            )
            val store = ElmStore(
                initialState = TopicListNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = TopicListStatus.Loaded,
                ),
                reducer = topicListReducer,
                actor = topicListActor,
            )

            val newItems = listOf(
                mockk<TopicListUiModel> { every { id() } returns "4" },
                mockk<TopicListUiModel> { every { id() } returns "5" },
            )

            every {
                topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 2, isLoadMore = true))
            } returns flow {
                emit(
                    TopicListNamespace.Event.OnLoadMorePageLoaded(
                        list = newItems,
                        hasNextPage = true,
                    )
                )
            }

            store.states.drop(1).test {
                store.accept(TopicListNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        status = TopicListStatus.Loaded,
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
                mockk<TopicListUiModel> { every { id() } returns "1" },
                mockk<TopicListUiModel> { every { id() } returns "2" },
            )
            val store = ElmStore(
                initialState = TopicListNamespace.State(
                    list = initialItems,
                    currentPage = 1,
                    status = TopicListStatus.Loaded,
                ),
                reducer = topicListReducer,
                actor = topicListActor,
            )

            every {
                topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 2, isLoadMore = true))
            } returns flow {
                emit(
                    TopicListNamespace.Event.OnLoadMorePageLoaded(
                        list = emptyList(),
                        hasNextPage = false,
                    )
                )
            }

            store.states.drop(1).test {
                store.accept(TopicListNamespace.Event.LoadMore)

                val loadedState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        status = TopicListStatus.Loaded,
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
            mockk<TopicListUiModel> { every { id() } returns "1" },
        )
        val store = ElmStore(
            initialState = TopicListNamespace.State(
                list = initialItems,
                currentPage = 1,
                status = TopicListStatus.Loaded,
            ),
            reducer = topicListReducer,
            actor = topicListActor,
        )

        every {
            topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 2, isLoadMore = true))
        } returns flow {
            emit(TopicListNamespace.Event.OnLoadMorePageError("", Throwable()))
        }

        store.effects.test {
            store.accept(TopicListNamespace.Event.LoadMore)

            val errorEffect = awaitItem()
            assertEquals(TopicListNamespace.Effect.Error, errorEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh AND it succeeds SHOULD show data with Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = TopicListNamespace.State(),
            reducer = topicListReducer,
            actor = topicListActor,
        )

        val items = listOf(
            mockk<TopicListUiModel>(),
            mockk<TopicListUiModel>(),
        )

        every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(
                        TopicListNamespace.Event.OnPageLoaded(
                            list = items,
                            hasNextPage = true,
                        )
                    )
                }

        store.states.drop(1).test {
            store.accept(TopicListNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    currentPage = 1,
                    hasNextPage = true,
                    isPullDownRefreshing = false,
                    list = items,
                    status = TopicListStatus.Loaded,
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
                initialState = TopicListNamespace.State(),
                reducer = topicListReducer,
                actor = topicListActor,
            )

            every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                    flow {
                        emit(
                            TopicListNamespace.Event.OnPageLoaded(
                                list = emptyList(),
                                hasNextPage = false,
                            )
                        )
                    }

            store.states.drop(1).test {
                store.accept(TopicListNamespace.Event.Refresh)

                val loadingState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        isPullDownRefreshing = true,
                        currentPage = 1,
                    ),
                    loadingState,
                )

                val loadedState = awaitItem()
                assertEquals(
                    TopicListNamespace.State(
                        status = TopicListStatus.Empty,
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
            initialState = TopicListNamespace.State(),
            reducer = topicListReducer,
            actor = topicListActor,
        )

        every { topicListActor.execute(TopicListNamespace.Command.LoadPage(page = 1)) } returns
                flow {
                    emit(TopicListNamespace.Event.OnLoadPageError(message = "", error = Throwable()))
                }

        store.states.drop(1).test {
            store.accept(TopicListNamespace.Event.Refresh)

            val loadingState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    isPullDownRefreshing = true,
                    currentPage = 1,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                TopicListNamespace.State(
                    status = TopicListStatus.Error,
                    isPullDownRefreshing = false,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
