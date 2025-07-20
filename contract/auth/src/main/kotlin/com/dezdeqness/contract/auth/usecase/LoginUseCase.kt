package com.dezdeqness.contract.auth.usecase

import com.dezdeqness.contract.auth.model.AuthorizationState
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.user.repository.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(code: String): Result<Boolean> {
        val loginResult = authRepository.login(code)

        if (loginResult.isFailure) {
            return Result.failure(loginResult.exceptionOrNull() ?: Throwable("Login failure"))
        }

        val token = loginResult.getOrNull() ?: return Result.failure(Throwable("Token failure"))
        val tokenResult = authRepository.saveToken(token)

        if (tokenResult.isFailure) {
            return Result.failure(tokenResult.exceptionOrNull() ?: Throwable("Token failure"))
        }

        val profileResult = userRepository.getProfileRemote()

        if (profileResult.isFailure) {
            authRepository.clearToken()
            return Result.failure(profileResult.exceptionOrNull() ?: Throwable("Profile failure"))
        }

        userRepository.saveProfileLocal(
            profileResult.getOrNull() ?: return Result.failure(Throwable("Save profile failure"))
        )

        authRepository.emitAuthorizationState(AuthorizationState.LoggedIn)

        return Result.success(true)
    }

}
