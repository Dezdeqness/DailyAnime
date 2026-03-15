package com.dezdeqness.contract.auth.model

sealed class SessionState {
    data object Loading : SessionState()
    data object Unauthenticated : SessionState()
    data class Authenticated(
        val userId: Long,
        val nickname: String,
        val avatar: String,
        val accountType: AccountType = AccountType.SHIKIMORI,
    ) : SessionState()
}
