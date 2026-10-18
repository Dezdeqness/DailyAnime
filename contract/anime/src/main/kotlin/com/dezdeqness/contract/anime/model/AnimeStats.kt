package com.dezdeqness.contract.anime.model

data class ScoreStat(
    val score: Int,
    val count: Int,
)

data class StatusStat(
    val status: UserRateStatus,
    val count: Int,
)
