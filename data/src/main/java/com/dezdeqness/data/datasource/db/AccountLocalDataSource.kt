package com.dezdeqness.data.datasource.db

import com.dezdeqness.contract.user.model.AccountEntity

interface AccountLocalDataSource {

    fun saveAccount(accountEntity: AccountEntity)

    fun getAccount(): AccountEntity?

    fun deleteAccount()

}
