package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.repository.AccountRepository

class RefreshTokenUseCase(
    private val accountRepository: AccountRepository,
) {

    operator fun invoke(): Result<String> {

        val isExpired = accountRepository.isSessionExpired()

        if (!isExpired) {
            return Result.success("")
        }

        val refreshResult = accountRepository.refresh()
        if (refreshResult.isFailure) {
            return Result.failure(refreshResult.exceptionOrNull() ?: Throwable("Refresh failure"))
        }

        val token = refreshResult.getOrNull() ?: return Result.failure(Throwable("Token failure"))
        val tokenResult = accountRepository.saveToken(token)

        if (tokenResult.isFailure) {
            return Result.failure(tokenResult.exceptionOrNull() ?: Throwable("Save token failure"))
        }

        return Result.success(token.accessToken)
    }

}
