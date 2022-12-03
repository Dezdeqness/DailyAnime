package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AccountRemote
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.data.model.db.StatusLocal
import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.StatusEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountMapper @Inject constructor() {

    fun fromResponse(item: AccountRemote) =
        AccountEntity(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name.orEmpty(),
            sex = item.sex,
            fullAnimeStatusesEntity = FullAnimeStatusesEntity(
                item.stats?.fullStatuses?.anime?.map { status ->
                    StatusEntity(
                        id = status.id,
                        groupedId = status.groupedId,
                        name = status.name,
                        size = status.size,
                        type = status.type,
                    )
                } ?: listOf()),
        )

    fun fromDatabase(item: AccountLocal) =
        AccountEntity(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name,
            sex = item.sex,
            fullAnimeStatusesEntity = FullAnimeStatusesEntity(
                item.anime?.map { status ->
                    StatusEntity(
                        id = status.statusId,
                        groupedId = status.groupedId,
                        name = status.name,
                        size = status.size,
                        type = status.type,
                    )
                } ?: listOf()),
        )

    fun toDatabase(item: AccountEntity) =
        AccountLocal(
            id = item.id,
            nickname = item.nickname,
            avatar = item.avatar,
            lastOnline = item.lastOnline,
            name = item.name,
            sex = item.sex,
            anime =
            item.fullAnimeStatusesEntity.list.map { status ->
                StatusLocal(
                    statusId = status.id,
                    groupedId = status.groupedId,
                    name = status.name,
                    size = status.size,
                    type = status.type,
                )
            },
        )
}
