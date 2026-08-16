package com.dezdeqness.domain.auth.usecases

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.auth.usecases.RefreshTokenUseCase
import javax.inject.Inject

class RefreshTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : RefreshTokenUseCase {

    override fun invoke(): Result<String> {
        if (!authRepository.isSessionExpired()) {
            return Result.success(authRepository.getToken().accessToken)
        }

        return authRepository.refresh()
            .onSuccess { token ->
                authRepository.saveToken(token)
            }
            .map { it.accessToken }
    }
}
