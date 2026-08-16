package com.dezdeqness.contract.auth.usecases

interface LoginUseCase {

    suspend operator fun invoke(code: String): Result<Boolean>
}
