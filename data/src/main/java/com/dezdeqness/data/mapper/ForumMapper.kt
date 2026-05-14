package com.dezdeqness.data.mapper

import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.data.model.ForumRemote
import javax.inject.Inject

class ForumMapper @Inject constructor() {

    fun fromResponse(remote: ForumRemote): ForumEntity = ForumEntity(
        id = remote.id,
        position = remote.position,
        name = remote.name,
        permalink = remote.permalink,
        url = remote.url,
    )
}
