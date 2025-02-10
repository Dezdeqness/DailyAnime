package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.AuthorizationState
import com.dezdeqness.domain.repository.AccountRepository

class LogoutUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke() =
        accountRepository
            .logout()
            .onSuccess {
                accountRepository.deleteAccountLocal()
                accountRepository.clearToken()
                accountRepository.clearUserCookie()

                accountRepository.emitAuthorizationState(AuthorizationState.LoggedOut)
            }
}
