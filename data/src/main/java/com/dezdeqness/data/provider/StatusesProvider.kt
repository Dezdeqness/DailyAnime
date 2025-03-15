package com.dezdeqness.data.provider

import android.util.Log
import com.dezdeqness.domain.model.StatusEntity
import javax.inject.Inject

class StatusesProvider @Inject constructor() {

    fun getStatuses(): List<StatusEntity> {
        Log.d("test", "getStatuses")

        return listOf(
            StatusEntity(
                id = 0,
                groupedId = "planned",
                name = "planned",
                size = 0,
                type = "Anime"
            ),
            StatusEntity(
                id = 1,
                groupedId = "watching",
                name = "watching",
                size = 0,
                type = "Anime"
            ),
            StatusEntity(
                id = 9,
                groupedId = "rewatching",
                name = "rewatching",
                size = 0,
                type = "Anime"
            ),
            StatusEntity(
                id = 2,
                groupedId = "completed",
                name = "completed",
                size = 0,
                type = "Anime"
            ),
            StatusEntity(
                id = 3,
                groupedId = "on_hold",
                name = "on_hold",
                size = 0,
                type = "Anime"
            ),
            StatusEntity(
                id = 4,
                groupedId = "dropped",
                name = "dropped",
                size = 0,
                type = "Anime"
            ),
        )
    }

}
