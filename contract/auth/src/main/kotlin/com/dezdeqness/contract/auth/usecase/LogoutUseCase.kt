package com.dezdeqness.contract.auth.usecase

import com.dezdeqness.contract.auth.model.AuthorizationState
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.user.repository.UserRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() =
        authRepository
            .logout()
            .onSuccess {
                userRepository.deleteAccountLocal()
                authRepository.clearToken()
                userRepository.clearUserCookie()

                authRepository.emitAuthorizationState(AuthorizationState.LoggedOut)
            }
}
