package com.dezdeqness.contract.user.repository

import com.dezdeqness.contract.user.model.AccountEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getProfileRemote(): Result<AccountEntity>

    fun getProfileDetails(): Flow<Result<AccountEntity>>

    fun getProfileLocal(): AccountEntity?

    fun saveProfileLocal(accountEntity: AccountEntity)

    fun deleteAccountLocal()

    fun clearUserCookie()
}
