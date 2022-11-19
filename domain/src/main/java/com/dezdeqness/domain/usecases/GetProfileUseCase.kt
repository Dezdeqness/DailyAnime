package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.repository.AccountRepository
import kotlinx.coroutines.flow.flow

class GetProfileUseCase(
    private val accountRepository: AccountRepository,
) {

    operator fun invoke() = flow {
        val localProfile = accountRepository.getProfileLocal()
        if (localProfile != null) {
            emit(Result.success(localProfile))
        }

        emit(accountRepository.getProfileRemote())
    }

}
