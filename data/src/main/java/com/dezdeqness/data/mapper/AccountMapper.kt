package com.dezdeqness.data.mapper

import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.data.core.TimestampConverter
import com.dezdeqness.data.model.AccountRemote
import com.dezdeqness.data.model.HistoryRemote
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.data.model.db.StatsLocal
import com.dezdeqness.data.model.db.StatusLocal
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.user.model.FullAnimeStatusesEntity
import com.dezdeqness.contract.user.model.StatsItemEntity
import com.dezdeqness.contract.user.model.StatusEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountMapper @Inject constructor(
    private val timestampConverter: TimestampConverter,
) {

    fun fromResponse(item: AccountRemote) =
        AccountEntity(
            id = item.id,
            nickname = item.nickname,
            avatar = item.image?.x160.orEmpty(),
            lastOnline = item.lastOnline,
            name = item.name.orEmpty(),
            sex = item.sex.orEmpty(),
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
            scores = item.stats?.scores?.items?.map { score ->
                StatsItemEntity(
                    name = score.name,
                    value = score.value,
                )
            } ?: listOf(),
            types = item.stats?.types?.items?.map { type ->
                StatsItemEntity(
                    name = type.name,
                    value = type.value,
                )
            } ?: listOf(),
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

    fun fromResponse(item: HistoryRemote) =
        HistoryEntity(
            id = item.id,
            description = item.description,
            createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.createdAt),
            itemId = item.historyItem?.id ?: 0,
            itemName = item.historyItem?.name.orEmpty(),
            itemRussian = item.historyItem?.russian.orEmpty(),
            itemUrl = item.historyItem?.url.orEmpty(),
            image = item.historyItem?.imageRemote?.original.orEmpty(),
        )

}
