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
        refreshResult.onFailure {
            return Result.failure(it)
        }

        val token = refreshResult.getOrElse {
            return Result.failure(it)
        }
        val tokenResult = accountRepository.saveToken(token)
        tokenResult.onFailure {
            return Result.failure(it)
        }

        return Result.success(token.accessToken)
    }

}
