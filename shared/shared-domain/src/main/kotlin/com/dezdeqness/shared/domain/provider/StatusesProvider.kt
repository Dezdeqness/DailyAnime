package com.dezdeqness.shared.domain.provider

import com.dezdeqness.shared.domain.model.StatusEntity

class StatusesProvider {

    fun getStatuses(): List<StatusEntity> {
        return listOf(
            StatusEntity(
                id = 0,
                groupedId = "planned",
                name = "planned",
                size = 0,
                type = "Anime",
            ),
            StatusEntity(
                id = 1,
                groupedId = "watching",
                name = "watching",
                size = 0,
                type = "Anime",
            ),
            StatusEntity(
                id = 9,
                groupedId = "rewatching",
                name = "rewatching",
                size = 0,
                type = "Anime",
            ),
            StatusEntity(
                id = 2,
                groupedId = "completed",
                name = "completed",
                size = 0,
                type = "Anime",
            ),
            StatusEntity(
                id = 3,
                groupedId = "on_hold",
                name = "on_hold",
                size = 0,
                type = "Anime",
            ),
            StatusEntity(
                id = 4,
                groupedId = "dropped",
                name = "dropped",
                size = 0,
                type = "Anime",
            ),
        )
    }
}
