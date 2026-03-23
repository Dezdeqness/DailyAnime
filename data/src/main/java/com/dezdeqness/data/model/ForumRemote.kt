package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel

data class ForumRemote(
    val id: Long,
    val position: Long,
    val name: String,
    val permalink: String,
    val url: String,
) : DataModel.Api
