package com.dezdeqness.data.mapper

import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.model.RoleRemote
import com.dezdeqness.contract.anime.model.RoleEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleMapper @Inject constructor(
    private val characterMapper: CharacterMapper,
) {

    fun fromResponse(items: List<RoleRemote>): List<RoleEntity> {
        val list = items
            .filter { it.character != null }
            .filter { it.roles.contains(MAIN_ROLE) || it.roles.contains(SUPPORTING_ROLE) }
            .map { item ->
                RoleEntity(
                    roles = item.roles,
                    rolesRussian = item.rolesRussian,
                    character = characterMapper.fromResponse(item.character!!),
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
