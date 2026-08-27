package com.dezdeqness.feature.auth.data

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.AccountType
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.auth.usecases.LoginUseCase
import com.dezdeqness.contract.auth.usecases.LogoutUseCase
import com.dezdeqness.contract.auth.usecases.RefreshTokenUseCase
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.contract.user.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
internal class SessionManagerImpl @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val accountSessionDao: AccountSessionDao,
    private val favouriteRepository: FavouriteRepository,
) : SessionManager {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    override val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val refreshMutex = Mutex()

    override val isAuthorized: Boolean
        get() = _sessionState.value is SessionState.Authenticated

    override suspend fun login(code: String): Result<Unit> {
        val result = loginUseCase(code)

        if (result.isSuccess) {
            favouriteRepository.clearCache()
            val state = resolveFromLocal()
            if (state != null) {
                val authenticated = state as SessionState.Authenticated
                accountSessionDao.deactivateAll()
                accountSessionDao.insertAccount(
                    AccountSessionLocal(
                        id = authenticated.userId.toString(),
                        accountType = AccountType.SHIKIMORI.name,
                        isActive = true,
                    ),
                )
                _sessionState.value = state
            }
        }

        return result.map { }
    }

    override suspend fun logout(): Result<Unit> {
        val activeAccount = accountSessionDao.getActiveAccount()
        val result = logoutUseCase()

        if (result.isSuccess) {
            if (activeAccount != null) {
                accountSessionDao.deleteAccount(activeAccount.id)
            }
            favouriteRepository.clearCache()
            _sessionState.value = SessionState.Unauthenticated
        }

        return result.map { }
    }

    override suspend fun restoreSession() {
        val activeAccount = accountSessionDao.getActiveAccount()

        if (activeAccount == null || !authRepository.isAuthorized()) {
            _sessionState.value = SessionState.Unauthenticated
            return
        }

        if (authRepository.isSessionExpired()) {
            val refreshResult = getValidToken()
            if (refreshResult.isFailure) {
                accountSessionDao.deactivateAccount(activeAccount.id)
                _sessionState.value = SessionState.Unauthenticated
                return
            }
        }

        _sessionState.value = resolveAuthenticatedState()
    }

    override suspend fun getValidToken(): Result<String> {
        return refreshMutex.withLock {
            refreshTokenUseCase()
                .onSuccess {
                    val state = resolveFromLocal()
                    if (state != null) {
                        _sessionState.value = state
                    }
                }
                .onFailure {
                    val activeAccount = accountSessionDao.getActiveAccount()
                    if (activeAccount != null) {
                        accountSessionDao.deactivateAccount(activeAccount.id)
                    }
                    _sessionState.value = SessionState.Unauthenticated
                }
        }
    }

    private fun resolveAuthenticatedState(): SessionState {
        var profile = userRepository.getProfileLocal()
        if (profile == null) {
            profile = userRepository.getProfileRemote().getOrNull()
            if (profile != null) {
                userRepository.saveProfileLocal(profile)
            }
        }

        return if (profile != null) {
            SessionState.Authenticated(
                userId = profile.id,
                nickname = profile.nickname,
                avatar = profile.avatar,
            )
        } else {
            SessionState.Unauthenticated
        }
    }

    private fun resolveFromLocal(): SessionState? {
        val profile = userRepository.getProfileLocal() ?: return null
        return SessionState.Authenticated(
            userId = profile.id,
            nickname = profile.nickname,
            avatar = profile.avatar,
        )
    }
}
