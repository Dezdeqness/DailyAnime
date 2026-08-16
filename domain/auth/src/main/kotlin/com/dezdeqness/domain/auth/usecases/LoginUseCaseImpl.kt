package com.dezdeqness.domain.auth.usecases

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.auth.usecases.LoginUseCase
import com.dezdeqness.contract.user.repository.UserRepository
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : LoginUseCase {

    override suspend fun invoke(code: String): Result<Boolean> {
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
            profileResult.getOrNull() ?: return Result.failure(Throwable("Save profile failure")),
        )

        return Result.success(true)
    }
}
