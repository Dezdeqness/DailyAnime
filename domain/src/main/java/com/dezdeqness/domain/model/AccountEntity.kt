package com.dezdeqness.domain.model

data class AccountEntity(
    val id: Long,
    val nickname: String,
    val avatar: String,
    val lastOnline: String,
    val name: String,
    val sex: String,
    val fullAnimeStatusesEntity: FullAnimeStatusesEntity,
    val scores: List<StatsItemEntity> = listOf(),
    val types: List<StatsItemEntity> = listOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AccountEntity) return false

        if (id != other.id) return false
        if (nickname != other.nickname) return false
        if (avatar != other.avatar) return false
        if (lastOnline != other.lastOnline) return false
        if (name != other.name) return false
        if (sex != other.sex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + lastOnline.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + sex.hashCode()
        return result
    }
}


data class FullAnimeStatusesEntity(
    val list: List<StatusEntity>
)

data class StatusEntity(
    val id: Long,
    val groupedId: String,
    val name: String,
    val size: Long,
    val type: String
)

data class StatsItemEntity(
    val name: String,
    val value: Int,
)
