package com.dezdeqness.feature.onboarding.flow.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.settings.models.OnboardingCompletedPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)

        coEvery {
            settingsRepository.setPreference(OnboardingCompletedPreference, any())
        } returns Unit
        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit

        viewModel = OnboardingViewModel(
            settingsRepository = settingsRepository,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
        )
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN next from welcome SHOULD move to genres`() {
        viewModel.onNext()
        assertEquals(OnboardingStep.GENRES, viewModel.step.value)
    }

    @Test
    fun `WHEN next through the flow SHOULD reach done`() {
        viewModel.onNext() // -> GENRES
        viewModel.onNext() // -> NOTIFICATIONS
        assertEquals(OnboardingStep.NOTIFICATIONS, viewModel.step.value)
        viewModel.onNext() // -> DONE
        assertEquals(OnboardingStep.DONE, viewModel.step.value)
    }

    @Test
    fun `WHEN next on the last step SHOULD stay on done`() {
        repeat(times = 5) { viewModel.onNext() }
        assertEquals(OnboardingStep.DONE, viewModel.step.value)
    }

    @Test
    fun `WHEN back from notifications SHOULD move to genres`() {
        viewModel.onNext() // -> GENRES
        viewModel.onNext() // -> NOTIFICATIONS
        viewModel.onBackClicked()
        assertEquals(OnboardingStep.GENRES, viewModel.step.value)
    }

    @Test
    fun `WHEN back from genres SHOULD move to welcome`() {
        viewModel.onNext() // -> GENRES
        viewModel.onBackClicked()
        assertEquals(OnboardingStep.WELCOME, viewModel.step.value)
    }

    @Test
    fun `WHEN back on welcome SHOULD stay on welcome`() {
        viewModel.onBackClicked()
        assertEquals(OnboardingStep.WELCOME, viewModel.step.value)
    }

    @Test
    fun `WHEN back on done SHOULD stay on done`() {
        repeat(times = 3) { viewModel.onNext() } // -> DONE
        viewModel.onBackClicked()
        assertEquals(OnboardingStep.DONE, viewModel.step.value)
    }

    @Test
    fun `WHEN skip clicked SHOULD complete onboarding and emit finish`() = runTest {
        viewModel.events.test {
            viewModel.onSkipClicked()
            assertEquals(OnboardingEvent.Finish, awaitItem())
            ensureAllEventsConsumed()
        }
        coVerify { settingsRepository.setPreference(OnboardingCompletedPreference, true) }
    }
}
