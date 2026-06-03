package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.auth.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Result<String> {
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
