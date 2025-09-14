package com.dezdeqness.contract.anime.model

data class RoleEntity(
    val roles: List<String>,
    val rolesRussian: List<String>,
    val character: CharacterEntity,
)
