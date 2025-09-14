package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.StudioRemote
import com.dezdeqness.contract.anime.model.StudioEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudioMapper @Inject constructor() {

    fun fromResponse(item: StudioRemote) =
        StudioEntity(
            id = item.id,
            name = item.name,
            filteredName = item.filteredName,
            real = item.real,
        )

}
