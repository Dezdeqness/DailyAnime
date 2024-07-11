package com.dezdeqness.data.datasource.db

import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.mapper.AccountMapper
import com.dezdeqness.domain.model.AccountEntity
import javax.inject.Inject

class AccountLocalDataSourceImpl @Inject constructor(
    private val accountMapper: AccountMapper,
    private val accountDao: AccountDao,
) : AccountLocalDataSource {

    override fun saveAccount(accountEntity: AccountEntity) {
        val accountLocal = accountMapper.toDatabase(accountEntity)
        accountDao.insertAccount(accountLocal)
    }

    override fun getAccount(): AccountEntity? {
        val accountLocal = accountDao.getAccount()
        return accountLocal?.let {
            accountMapper.fromDatabase(accountLocal)
        }
    }

    override fun deleteAccount() {
        accountDao.deleteAccount()
    }

}
