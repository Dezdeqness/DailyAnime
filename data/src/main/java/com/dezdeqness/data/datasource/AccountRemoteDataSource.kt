package com.dezdeqness.data.datasource

import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.auth.model.TokenEntity

interface AccountRemoteDataSource {

    fun getAuthorizationCodeUrl(): Result<String>

    fun login(code: String): Result<TokenEntity>

    fun logout(): Result<Boolean>

    fun refresh(token: String): Result<TokenEntity>

    fun getBriefAccountInfo(): Result<AccountEntity>

    fun getDetailsAccountInfo(userId: Long): Result<AccountEntity>

    fun getHistory(userId: Long, page: Int, limit: Int): Result<List<HistoryEntity>>

}
