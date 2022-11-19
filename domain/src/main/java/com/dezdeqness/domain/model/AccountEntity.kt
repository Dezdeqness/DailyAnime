package com.dezdeqness.domain.model

data class AccountEntity(
    val id: Long,
    val nickname: String,
    val avatar: String,
    val lastOnline: String,
    val name: String,
    val sex: String,
)
