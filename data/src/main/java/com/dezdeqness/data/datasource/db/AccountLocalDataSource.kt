package com.dezdeqness.data.datasource.db

import com.dezdeqness.domain.model.AccountEntity

interface AccountLocalDataSource {

    fun saveAccount(accountEntity: AccountEntity)

    fun getAccount(): AccountEntity?

    fun deleteAccount()

}
