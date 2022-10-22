package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class RoleRemote(
    @field:Json(name = "roles")
    val roles: List<String>,

    @field:Json(name = "roles_russian")
    val rolesRussian: List<String>,

    @field:Json(name = "character")
    val character: CharacterRemote? = null,
) : DataModel.Api
