package com.dezdeqness.domain.auth.usecases

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.auth.usecases.LogoutUseCase
import com.dezdeqness.contract.user.repository.UserRepository
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : LogoutUseCase {

    override suspend fun invoke(): Result<Boolean> = authRepository
        .logout()
        .onSuccess {
            userRepository.deleteAccountLocal()
            authRepository.clearToken()
            userRepository.clearUserCookie()
        }
}
