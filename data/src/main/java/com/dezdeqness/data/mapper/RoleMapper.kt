package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.data.DetailsQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleMapper @Inject constructor(
    private val characterMapper: CharacterMapper,
) {

    fun fromResponseGraphql(roles: List<DetailsQuery.CharacterRole>?): List<RoleEntity> {
        if (roles == null || roles.isEmpty()) {
            return listOf()
        }

        val list = roles
            .filter { it.character.isAnime }
            .filter { it.rolesEn.contains(MAIN_ROLE) || it.rolesEn.contains(SUPPORTING_ROLE) }
            .map { item ->
                RoleEntity(
                    roles = item.rolesEn,
                    rolesRussian = item.rolesRu,
                    character = characterMapper.fromResponse(item.character)
                )
            }

        val main = list
            .filter { it.rolesRussian.contains(MAIN_ROLE) }
            .sortedBy {
                it.character.russian.ifEmpty {
                    it.character.name
                }
            }
        val supporting =
            list.filter { it.rolesRussian.contains(SUPPORTING_ROLE) }.sortedBy {
                it.character.russian.ifEmpty {
                    it.character.name
                }
            }


        return main + supporting

    }

    companion object {
        private const val MAIN_ROLE = "Main"
        private const val SUPPORTING_ROLE = "Supporting"
    }

}
