package com.dezdeqness.data.datasource.db

import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.mapper.AccountDbMapper
import javax.inject.Inject

class AccountLocalDataSourceImpl @Inject constructor(
    private val accountDbMapper: AccountDbMapper,
    private val accountDao: AccountDao,
) : AccountLocalDataSource {

    override fun saveAccount(accountEntity: AccountEntity) {
        val accountLocal = accountDbMapper.toDatabase(accountEntity)
        accountDao.insertAccount(accountLocal)
    }

    override fun getAccount(): AccountEntity? {
        val accountLocal = accountDao.getAccount()
        return accountLocal?.let {
            accountDbMapper.fromDatabase(accountLocal)
        }
    }

    override fun deleteAccount() {
        accountDao.deleteAccount()
    }
}
