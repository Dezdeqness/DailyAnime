package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.AuthorizationState
import com.dezdeqness.domain.repository.AccountRepository

class LoginUseCase(
    private val accountRepository: AccountRepository,

) {

    suspend operator fun invoke(code: String): Result<Boolean> {
        val loginResult = accountRepository.login(code)

        if (loginResult.isFailure) {
            return Result.failure(loginResult.exceptionOrNull() ?: Throwable("Login failure"))
        }

        val token = loginResult.getOrNull() ?: return Result.failure(Throwable("Token failure"))
        val tokenResult = accountRepository.saveToken(token)

        if (tokenResult.isFailure) {
            return Result.failure(tokenResult.exceptionOrNull() ?: Throwable("Token failure"))
        }

        val profileResult = accountRepository.getProfileRemote()

        if (profileResult.isFailure) {
            accountRepository.clearToken()
            return Result.failure(profileResult.exceptionOrNull() ?: Throwable("Profile failure"))
        }

        accountRepository.saveProfileLocal(
            profileResult.getOrNull() ?: return Result.failure(Throwable("Save profile failure"))
        )

        accountRepository.emitAuthorizationState(AuthorizationState.LoggedIn)

        return Result.success(true)
    }

}
