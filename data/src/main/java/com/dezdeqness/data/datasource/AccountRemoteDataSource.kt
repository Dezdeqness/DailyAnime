package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.domain.model.TokenEntity

interface AccountRemoteDataSource {

    fun getAuthorizationCodeUrl(): Result<String>

    fun login(code: String): Result<TokenEntity>

    fun refresh(token: String): Result<TokenEntity>

    fun getBriefAccountInfo(token: String): Result<AccountEntity>

    fun getDetailsAccountInfo(userId: Long): Result<AccountEntity>

    fun getHistory(userId: Long, page: Int, limit: Int): Result<List<HistoryEntity>>

}
