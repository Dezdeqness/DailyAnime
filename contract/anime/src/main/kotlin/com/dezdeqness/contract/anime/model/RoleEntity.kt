package com.dezdeqness.contract.anime.model

data class RoleEntity(
    val roles: List<CharacterRoleKind>,
    val character: CharacterEntity,
)
