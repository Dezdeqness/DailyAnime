package com.dezdeqness.feature.forum.presentation.store

import app.cash.turbine.test
import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.foundation.test.MainDispatcherExtension
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
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
class ForumReducerTest {

    @MockK(relaxed = true)
    private lateinit var forumActor: ForumActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN InitialLoad AND forums loaded SHOULD show Loaded status`(): Unit = runTest {
        val store = ElmStore(
            initialState = ForumNamespace.State(),
            reducer = forumReducer,
            actor = forumActor,
        )

        val items = listOf(
            mockk<ForumUiModel>(),
            mockk<ForumUiModel>(),
        )

        every { forumActor.execute(ForumNamespace.Command.Load) } returns
                flow { emit(ForumNamespace.Event.OnLoaded(list = items)) }

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loading,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                loadingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loaded,
                    list = items,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN InitialLoad AND forums empty SHOULD show Empty status`(): Unit = runTest {
        val store = ElmStore(
            initialState = ForumNamespace.State(),
            reducer = forumReducer,
            actor = forumActor,
        )

        every { forumActor.execute(ForumNamespace.Command.Load) } returns
                flow { emit(ForumNamespace.Event.OnLoaded(list = emptyList())) }

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loading,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                loadingState,
            )

            val emptyState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Empty,
                    list = emptyList(),
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                emptyState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN InitialLoad AND forums fail SHOULD show Error status`(): Unit = runTest {
        val store = ElmStore(
            initialState = ForumNamespace.State(),
            reducer = forumReducer,
            actor = forumActor,
        )

        every { forumActor.execute(ForumNamespace.Command.Load) } returns
                flow { emit(ForumNamespace.Event.OnError(message = "", error = Throwable())) }

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.InitialLoad)

            val loadingState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loading,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                loadingState,
            )

            val errorState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Error,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                errorState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh AND forums loaded SHOULD reset isPullDownRefreshing`(): Unit = runTest {
        val initialItems = listOf(mockk<ForumUiModel>())
        val store = ElmStore(
            initialState = ForumNamespace.State(
                status = ForumStatus.Loaded,
                list = initialItems,
                hotTopicsStatus = HotTopicsStatus.Loaded,
            ),
            reducer = forumReducer,
            actor = forumActor,
        )

        val refreshedItems = listOf(mockk<ForumUiModel>(), mockk<ForumUiModel>())

        every { forumActor.execute(ForumNamespace.Command.Load) } returns
                flow { emit(ForumNamespace.Event.OnLoaded(list = refreshedItems)) }

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.Refresh)

            val refreshingState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loaded,
                    list = initialItems,
                    isPullDownRefreshing = true,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                refreshingState,
            )

            val loadedState = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    status = ForumStatus.Loaded,
                    list = refreshedItems,
                    isPullDownRefreshing = false,
                    hotTopicsStatus = HotTopicsStatus.Loading,
                ),
                loadedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Refresh fails AND list is not empty SHOULD emit Error effect`(): Unit = runTest {
        val initialItems = listOf(mockk<ForumUiModel>())
        val store = ElmStore(
            initialState = ForumNamespace.State(
                status = ForumStatus.Loaded,
                list = initialItems,
                hotTopicsStatus = HotTopicsStatus.Loaded,
            ),
            reducer = forumReducer,
            actor = forumActor,
        )

        every { forumActor.execute(ForumNamespace.Command.Load) } returns
                flow { emit(ForumNamespace.Event.OnError(message = "", error = Throwable())) }

        store.effects.test {
            store.accept(ForumNamespace.Event.Refresh)

            val effect = awaitItem()
            assertEquals(ForumNamespace.Effect.Error, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnHotTopicsLoaded SHOULD update hotTopics and set status Loaded`(): Unit = runTest {
        val store = ElmStore(
            initialState = ForumNamespace.State(hotTopicsStatus = HotTopicsStatus.Loading),
            reducer = forumReducer,
            actor = forumActor,
        )

        val topics = listOf(mockk<TopicPresentationModel>(), mockk<TopicPresentationModel>())

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.OnHotTopicsLoaded(topics = topics))

            val state = awaitItem()
            assertEquals(
                ForumNamespace.State(
                    hotTopics = topics,
                    hotTopicsStatus = HotTopicsStatus.Loaded,
                ),
                state,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnHotTopicsError SHOULD set hotTopicsStatus Error`(): Unit = runTest {
        val store = ElmStore(
            initialState = ForumNamespace.State(hotTopicsStatus = HotTopicsStatus.Loading),
            reducer = forumReducer,
            actor = forumActor,
        )

        store.states.drop(1).test {
            store.accept(ForumNamespace.Event.OnHotTopicsError(message = "", error = Throwable()))

            val state = awaitItem()
            assertEquals(
                ForumNamespace.State(hotTopicsStatus = HotTopicsStatus.Error),
                state,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
