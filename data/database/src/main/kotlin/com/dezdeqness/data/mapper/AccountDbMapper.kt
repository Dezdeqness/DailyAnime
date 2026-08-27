package com.dezdeqness.data.mapper

import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.user.model.FullAnimeStatusesEntity
import com.dezdeqness.contract.user.model.StatsItemEntity
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.data.model.db.StatsLocal
import com.dezdeqness.data.model.db.StatusLocal
import com.dezdeqness.shared.domain.model.StatusEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDbMapper @Inject constructor() {

    fun fromDatabase(item: AccountLocal) = AccountEntity(
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
            } ?: listOf(),
        ),
        scores = item.scores?.map { score ->
            StatsItemEntity(
                name = score.name,
                value = score.value,
            )
        } ?: listOf(),
        types = item.types?.map { type ->
            StatsItemEntity(
                name = type.name,
                value = type.value,
            )
        } ?: listOf(),
    )

    fun toDatabase(item: AccountEntity) = AccountLocal(
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
        scores = item.scores.map { score ->
            StatsLocal(
                name = score.name,
                value = score.value,
            )
        },
        types = item.types.map { type ->
            StatsLocal(
                name = type.name,
                value = type.value,
            )
        },
    )
}
