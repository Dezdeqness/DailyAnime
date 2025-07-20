package com.dezdeqness.contract.auth.usecase

import com.dezdeqness.contract.auth.repository.AuthRepository

class RefreshTokenUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Result<String> {

        val isExpired = authRepository.isSessionExpired()

        if (!isExpired) {
            return Result.success("")
        }

        val refreshResult = authRepository.refresh()
        refreshResult.onFailure {
            return Result.failure(it)
        }

        val token = refreshResult.getOrElse {
            return Result.failure(it)
        }
        val tokenResult = authRepository.saveToken(token)
        tokenResult.onFailure {
            return Result.failure(it)
        }

        return Result.success(token.accessToken)
    }

}
