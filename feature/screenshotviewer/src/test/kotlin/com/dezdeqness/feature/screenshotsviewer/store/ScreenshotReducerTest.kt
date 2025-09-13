package com.dezdeqness.feature.screenshotsviewer.store

import app.cash.turbine.test
import com.dezdeqness.data.BuildConfig
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.NoOpActor
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ScreenshotReducerTest {
    @Test
    fun `WHEN IndexChanged SHOULD update to new index`(): Unit = runTest {
        val store = ElmStore(
            initialState = State(
                screenshotsList = listOf("url1", "url2"),
                index = 0,
            ),
            reducer = screenshotReducer,
            actor = NoOpActor()
        )

        val expectedIndex = 1

        store.states.drop(1).test {
            store.accept(Event.IndexChanged(index = expectedIndex))

            val state = awaitItem()
            assertEquals(expectedIndex, state.index)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN ShareUrlClicked emits ShareUrl with raw url SHOULD emit effect with BASE_URL`(): Unit = runTest {
        val screenshotUrl = "image.jpg"
        val expectedUrl = BuildConfig.BASE_URL + screenshotUrl

        val store = ElmStore(
            initialState = State(
                screenshotsList = listOf(screenshotUrl),
                index = 0,
            ),
            reducer = screenshotReducer,
            actor = NoOpActor()
        )

        store.effects.test {
            store.accept(Event.ShareUrlClicked)

            val effect = awaitItem()
            assertEquals(Effect.ShareUrl(url = expectedUrl), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN ShareUrlClicked emits ShareUrl SHOULD emit effect with BASE_URL`(): Unit = runTest {
        val screenshotUrl = BuildConfig.BASE_URL + "image.jpg"

        val store = ElmStore(
            initialState = State(
                screenshotsList = listOf(screenshotUrl),
                index = 0,
            ),
            reducer = screenshotReducer,
            actor = NoOpActor()
        )

        store.effects.test {
            store.accept(Event.ShareUrlClicked)

            val effect = awaitItem()
            assertEquals(Effect.ShareUrl(url = screenshotUrl), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

}
