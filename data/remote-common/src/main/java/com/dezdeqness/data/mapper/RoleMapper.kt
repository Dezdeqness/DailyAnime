package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.CharacterRoleKind
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.data.DetailsQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleMapper @Inject constructor(
    private val characterMapper: CharacterMapper,
) {

    fun fromResponseGraphql(roles: List<DetailsQuery.CharacterRole>?): List<RoleEntity> {
        if (roles.isNullOrEmpty()) {
            return listOf()
        }

        val list = roles
            .filter { it.character.isAnime }
            .map { item ->
                RoleEntity(
                    roles = item.rolesEn.mapNotNull(roleKinds::get),
                    character = characterMapper.fromResponse(item.character),
                )
            }

        val main = list
            .filter { it.roles.contains(CharacterRoleKind.MAIN) }
            .sortedBy {
                it.character.russian.ifEmpty {
                    it.character.name
                }
            }
        val supporting = list
            .filter { !it.roles.contains(CharacterRoleKind.MAIN) && it.roles.contains(CharacterRoleKind.SUPPORTING) }
            .sortedBy {
                it.character.russian.ifEmpty {
                    it.character.name
                }
            }

        return main + supporting
    }

    companion object {
        private const val MAIN_ROLE = "Main"
        private const val SUPPORTING_ROLE = "Supporting"

        private val roleKinds = mapOf(
            MAIN_ROLE to CharacterRoleKind.MAIN,
            SUPPORTING_ROLE to CharacterRoleKind.SUPPORTING,
        )
    }
}
