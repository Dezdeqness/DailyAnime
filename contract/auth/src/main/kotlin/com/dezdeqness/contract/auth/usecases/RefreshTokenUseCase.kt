package com.dezdeqness.contract.auth.usecases

interface RefreshTokenUseCase {

    operator fun invoke(): Result<String>
}
