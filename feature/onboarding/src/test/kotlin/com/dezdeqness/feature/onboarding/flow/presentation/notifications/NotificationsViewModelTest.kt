package com.dezdeqness.feature.onboarding.flow.presentation.notifications

import com.dezdeqness.contract.settings.models.NotificationEnabledPreference
import com.dezdeqness.contract.settings.models.NotificationTimePreference
import com.dezdeqness.contract.settings.models.TimeEntity
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    @MockK
    private lateinit var workSchedulerManager: WorkSchedulerManager

    private lateinit var viewModel: NotificationsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)

        coEvery { settingsRepository.getPreference(NotificationEnabledPreference) } returns false
        coEvery {
            settingsRepository.getPreference(NotificationTimePreference)
        } returns TimeEntity(hours = 19, minutes = 0)
        coEvery { settingsRepository.setPreference(NotificationEnabledPreference, any()) } returns Unit
        coEvery { settingsRepository.setPreference(NotificationTimePreference, any()) } returns Unit
        coEvery { workSchedulerManager.scheduleDailyWork() } returns Unit
        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit

        viewModel = NotificationsViewModel(
            settingsRepository = settingsRepository,
            workSchedulerManager = workSchedulerManager,
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
    fun `WHEN initialized SHOULD seed from preferences`() = runTest {
        coEvery { settingsRepository.getPreference(NotificationEnabledPreference) } returns true
        coEvery {
            settingsRepository.getPreference(NotificationTimePreference)
        } returns TimeEntity(hours = 8, minutes = 30)
        viewModel = NotificationsViewModel(
            settingsRepository, workSchedulerManager,
            object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger,
        )
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.enabled)
        assertEquals(TimeEntity(hours = 8, minutes = 30), viewModel.uiState.value.time)
    }

    @Test
    fun `WHEN toggled and time changed SHOULD update state`() = runTest {
        advanceUntilIdle()

        viewModel.onToggled(true)
        viewModel.onTimeChanged(hours = 17, minutes = 45)
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.enabled)
        assertEquals(TimeEntity(hours = 17, minutes = 45), viewModel.uiState.value.time)
    }

    @Test
    fun `WHEN saved SHOULD persist prefs and schedule work`() = runTest {
        advanceUntilIdle()

        viewModel.onToggled(true)
        viewModel.onTimeChanged(hours = 17, minutes = 30)
        advanceUntilIdle()

        viewModel.save()
        advanceUntilIdle()

        coVerify {
            settingsRepository.setPreference(NotificationEnabledPreference, true)
            settingsRepository.setPreference(NotificationTimePreference, TimeEntity(17, 30))
            workSchedulerManager.scheduleDailyWork()
        }
    }
}
