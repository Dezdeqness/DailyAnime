package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AccountRemote
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.domain.model.AccountEntity
import javax.inject.Inject

class AccountMapper @Inject constructor() {

    fun fromResponse(item: AccountRemote) =
        AccountEntity(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name.orEmpty(),
            sex = item.sex,
        )

    fun fromDatabase(item: AccountLocal) =
        AccountEntity(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name,
            sex = item.sex,
        )

    fun toDatabase(item: AccountEntity) =
        AccountLocal(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name,
            sex = item.sex,
        )
}
