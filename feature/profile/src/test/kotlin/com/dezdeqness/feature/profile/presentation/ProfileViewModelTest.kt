package com.dezdeqness.feature.profile.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import com.dezdeqness.shared.presentation.model.AuthorizedUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var sessionManager: SessionManager

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: BaseMessageProvider

    private val sessionStateFlow = MutableStateFlow<SessionState>(SessionState.Loading)

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
        every { sessionManager.sessionState } returns sessionStateFlow
    }

    private fun createViewModel() = ProfileViewModel(
        userRepository = userRepository,
        sessionManager = sessionManager,
        messageConsumer = messageConsumer,
        messageProvider = messageProvider,
        coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
            override fun main() = Dispatchers.Main
            override fun io() = Dispatchers.Main
            override fun computation() = Dispatchers.Main
        },
        logger = logger,
    )

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN unauthenticated SHOULD emit unauthorized state`() = runTest {
        sessionStateFlow.value = SessionState.Unauthenticated

        viewModel = createViewModel()

        viewModel.profileStateFlow.test {
            advanceUntilIdle()

            assertEquals(AuthorizedUiState.Unauthorized, expectMostRecentItem().authorizedState)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN authenticated SHOULD fetch profile and emit authorized state`() = runTest {
        val account = mockk<AccountEntity> {
            every { id } returns 42L
            every { avatar } returns "avatarUrl"
            every { nickname } returns "Staria"
        }
        every { userRepository.getProfileDetails() } returns flowOf(Result.success(account))

        sessionStateFlow.value = SessionState.Authenticated(
            userId = 42L,
            nickname = "Staria",
            avatar = "avatarUrl",
        )

        viewModel = createViewModel()

        viewModel.profileStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(AuthorizedUiState.Authorized, state.authorizedState)
            assertEquals(42L, state.userId)
            assertEquals("Staria", state.nickname)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN profile fetch fails SHOULD show error message`() = runTest {
        every { userRepository.getProfileDetails() } returns flowOf(Result.failure(Exception("boom")))
        coEvery { messageConsumer.onErrorMessage(any()) } returns Unit
        every { messageProvider.getGeneralErrorMessage() } returns "error"

        sessionStateFlow.value = SessionState.Authenticated(userId = 1L, nickname = "n", avatar = "a")

        viewModel = createViewModel()

        viewModel.profileStateFlow.test {
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { messageConsumer.onErrorMessage("error") }
    }

    @Test
    fun `WHEN logout clicked SHOULD call session logout`() = runTest {
        sessionStateFlow.value = SessionState.Unauthenticated
        coEvery { sessionManager.logout() } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.onLogoutClicked()
        advanceUntilIdle()

        coVerify { sessionManager.logout() }
    }
}
