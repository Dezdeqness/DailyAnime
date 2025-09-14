package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Result<AccountEntity>> = userRepository.getProfileDetails()
}
