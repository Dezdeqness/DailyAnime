package com.dezdeqness.feature.userrate

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.UserRatesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRateViewModelTest {

    @MockK
    private lateinit var userRatesRepository: UserRatesRepository

    private lateinit var viewModel: UserRateViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = UserRateViewModel(
            userRatesRepository = userRatesRepository,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Unconfined
                override fun io() = Dispatchers.Unconfined
                override fun computation() = Dispatchers.Unconfined
            },
        )
    }

    @Test
    fun `WHEN onUserRateUpdated invoked SHOULD get user rate and update state`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onScoreChanged invoked SHOULD update score and check content changed`() = runTest {
        val userRate = defaultUserRateEntity()
        val score = 9L

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            viewModel.onScoreChanged(score)

            val scoreChangedState = awaitItem()
            assertEquals(score, scoreChangedState.score)

            val contentChangedState = awaitItem()
            assertEquals(true, contentChangedState.isContentChanged)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onEpisodesMinusClicked invoked SHOULD decrement episodes and check content changed`() =
        runTest {
            val userRate = defaultUserRateEntity()

            coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
            viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

            viewModel.userRateStateFlow.test {
                val loadedState = awaitItem()
                assertEquals(RATE_ID, loadedState.rateId)
                assertEquals(RATE_NAME, loadedState.title)
                assertEquals(userRate.status, loadedState.selectedStatus)
                assertEquals(userRate.episodes, loadedState.episode)
                assertEquals(userRate.score, loadedState.score)
                assertEquals(userRate.text, loadedState.comment)
                assertEquals(true, loadedState.isEditMode)

                viewModel.onEpisodesMinusClicked()

                val episodeMinusState = awaitItem()
                assertEquals(userRate.episodes.dec(), episodeMinusState.episode)

                val contentChangedState = awaitItem()
                assertEquals(true, contentChangedState.isContentChanged)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN onEpisodesPlusClicked invoked SHOULD decrement episodes and check content changed`() =
        runTest {
            val userRate = defaultUserRateEntity()

            coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
            viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

            viewModel.userRateStateFlow.test {
                val loadedState = awaitItem()
                assertEquals(RATE_ID, loadedState.rateId)
                assertEquals(RATE_NAME, loadedState.title)
                assertEquals(userRate.status, loadedState.selectedStatus)
                assertEquals(userRate.episodes, loadedState.episode)
                assertEquals(userRate.score, loadedState.score)
                assertEquals(userRate.text, loadedState.comment)
                assertEquals(true, loadedState.isEditMode)

                viewModel.onEpisodesPlusClicked()

                val episodePlusState = awaitItem()
                assertEquals(userRate.episodes.inc(), episodePlusState.episode)

                val contentChangedState = awaitItem()
                assertEquals(true, contentChangedState.isContentChanged)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN onEpisodesMinusClicked with zero episodes SHOULD not decrement below zero`() =
        runTest {
            val userRate = defaultUserRateEntity().copy(episodes = 0)

            coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
            viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

            viewModel.userRateStateFlow.test {
                val loadedState = awaitItem()
                assertEquals(RATE_ID, loadedState.rateId)
                assertEquals(RATE_NAME, loadedState.title)
                assertEquals(userRate.status, loadedState.selectedStatus)
                assertEquals(userRate.episodes, loadedState.episode)
                assertEquals(userRate.score, loadedState.score)
                assertEquals(userRate.text, loadedState.comment)
                assertEquals(true, loadedState.isEditMode)

                viewModel.onEpisodesMinusClicked()

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `WHEN onStatusChanged invoked SHOULD update status and check content changed`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            viewModel.onStatusChanged("completed")

            val statusChangedState = awaitItem()
            assertEquals("completed", statusChangedState.selectedStatus)

            val contentChangedState = awaitItem()
            assertEquals(true, contentChangedState.isContentChanged)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onCommentChanged invoked SHOULD update comment and check content changed`() =
        runTest {
            val userRate = defaultUserRateEntity()

            coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
            viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

            viewModel.userRateStateFlow.test {
                val loadedState = awaitItem()
                assertEquals(RATE_ID, loadedState.rateId)
                assertEquals(RATE_NAME, loadedState.title)
                assertEquals(userRate.status, loadedState.selectedStatus)
                assertEquals(userRate.episodes, loadedState.episode)
                assertEquals(userRate.score, loadedState.score)
                assertEquals(userRate.text, loadedState.comment)
                assertEquals(true, loadedState.isEditMode)

                viewModel.onCommentChanged("Updated comment")

                val statusChangedState = awaitItem()
                assertEquals("Updated comment", statusChangedState.comment)

                val contentChangedState = awaitItem()
                assertEquals(true, contentChangedState.isContentChanged)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN onResetButtonClicked invoked SHOULD reset to original user rate`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            viewModel.onStatusChanged("completed")

            val statusChangedState = awaitItem()
            assertEquals("completed", statusChangedState.selectedStatus)

            val contentChangedState = awaitItem()
            assertEquals(true, contentChangedState.isContentChanged)

            viewModel.onResetButtonClicked()

            val resetState = awaitItem()
            assertEquals(RATE_ID, resetState.rateId)
            assertEquals(RATE_NAME, resetState.title)
            assertEquals(userRate.status, resetState.selectedStatus)
            assertEquals(userRate.episodes, resetState.episode)
            assertEquals(userRate.score, resetState.score)
            assertEquals(userRate.text, resetState.comment)
            assertEquals(true, resetState.isEditMode)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onApplyButtonClicked with changes SHOULD emit EditRateUiModel`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.onStatusChanged("completed")

        viewModel.events.test {
            viewModel.onApplyButtonClicked()

            val event = awaitItem()
            assertEquals(RATE_ID, event.rateId)
            assertEquals("completed", event.status)
            assertEquals(8f, event.score)
        }
    }

    @Test
    fun `WHEN onSelectStatusClicked invoked SHOULD show status dialog`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            viewModel.onSelectStatusClicked()

            val selectStatusState = awaitItem()
            assertEquals(true, selectStatusState.isSelectStatusDialogShowed)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onCloseSelectStatusClicked invoked SHOULD hide status dialog`() = runTest {
        val userRate = defaultUserRateEntity()

        coEvery { userRatesRepository.getLocalUserRate(RATE_ID) } returns userRate
        viewModel.onUserRateUpdated(RATE_ID, RATE_NAME)

        viewModel.userRateStateFlow.test {
            val loadedState = awaitItem()
            assertEquals(RATE_ID, loadedState.rateId)
            assertEquals(RATE_NAME, loadedState.title)
            assertEquals(userRate.status, loadedState.selectedStatus)
            assertEquals(userRate.episodes, loadedState.episode)
            assertEquals(userRate.score, loadedState.score)
            assertEquals(userRate.text, loadedState.comment)
            assertEquals(true, loadedState.isEditMode)

            viewModel.onSelectStatusClicked()

            val selectStatusState = awaitItem()
            assertEquals(true, selectStatusState.isSelectStatusDialogShowed)

            viewModel.onCloseSelectStatusClicked()

            val closeSelectStatusState = awaitItem()
            assertEquals(false, closeSelectStatusState.isSelectStatusDialogShowed)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun defaultUserRateEntity() = UserRateEntity(
        id = RATE_ID,
        status = "watching",
        episodes = 5,
        score = 8,
        text = "Great anime",
        chapters = 0,
        volumes = 0,
        textHTML = "Great anime",
        rewatches = 0,
        createdAtTimestamp = 100_000_000,
        updatedAtTimestamp = 100_000_001,
        anime = null
    )

    companion object {
        private const val RATE_ID = 123L
        private const val RATE_NAME = "Test Anime"
    }
}
