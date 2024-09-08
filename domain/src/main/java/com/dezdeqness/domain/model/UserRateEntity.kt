package com.dezdeqness.domain.model

data class UserRateEntity(
    val id: Long,
    val score: Long,
    val status: String,
    val text: String,
    val episodes: Long,
    val chapters: Long,
    val volumes: Long,
    val textHTML: String,
    val rewatches: Long,
    val createdAtTimestamp: Long,
    val updatedAtTimestamp: Long,
    val anime: AnimeBriefEntity? = null
) {
    fun isEmptyUserRate() = id == NO_ID

    companion object {
        private const val NO_ID = -1L

        val EMPTY_USER_RATE = UserRateEntity(
            id = NO_ID,
            score = 0,
            status = "",
            text = "",
            episodes = 0,
            chapters = 0,
            volumes = 0,
            textHTML = "",
            rewatches = 0,
            updatedAtTimestamp = 0,
            createdAtTimestamp = 0,
        )
    }
}
