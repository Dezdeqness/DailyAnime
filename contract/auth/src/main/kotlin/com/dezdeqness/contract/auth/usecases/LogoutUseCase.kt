package com.dezdeqness.contract.auth.usecases

interface LogoutUseCase {

    suspend operator fun invoke(): Result<Boolean>
}
