package com.dezdeqness.feature.details.person.presentation.composer

import com.dezdeqness.contract.person.model.PersonDetailsEntity
import com.dezdeqness.contract.person.model.PersonRole
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.feature.details.person.R
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry
import com.dezdeqness.shared.presentation.utils.UrlNormalizer
import javax.inject.Inject

class PersonDetailsComposer @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val urlNormalizer: UrlNormalizer,
) {

    fun compose(entity: PersonDetailsEntity): List<DetailsSection> {
        val sections = mutableListOf<DetailsSection>()

        sections += HeaderSection(imageUrl = urlNormalizer.normalize(entity.image.original))
        sections += TitleSection(text = entity.russian.ifEmpty { entity.name })

        val briefItems = composeBriefInfo(entity)
        if (briefItems.isNotEmpty()) {
            sections += BriefInfoSection(items = briefItems)
        }

        sections += BottomSpacerSection

        return sections
    }

    private fun composeBriefInfo(entity: PersonDetailsEntity): List<BriefInfoEntry> {
        val list = mutableListOf<BriefInfoEntry>()

        if (entity.roles.isNotEmpty()) {
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.person_details_brief_role),
                info = entity.roles.joinToString(separator = ", ", transform = ::mapPersonRole),
            )
        }

        if (entity.birthOn.isNotEmpty()) {
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.person_details_brief_birth),
                info = entity.birthOn,
            )
        }

        if (entity.japanese.isNotEmpty()) {
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.person_details_brief_japanese),
                info = entity.japanese,
            )
        }

        return list
    }

    private fun mapPersonRole(role: PersonRole): String = resourceProvider.getString(
        when (role) {
            PersonRole.MANGAKA -> R.string.person_role_mangaka
            PersonRole.PRODUCER -> R.string.person_role_producer
            PersonRole.SEYU -> R.string.person_role_seyu
        },
    )
}
