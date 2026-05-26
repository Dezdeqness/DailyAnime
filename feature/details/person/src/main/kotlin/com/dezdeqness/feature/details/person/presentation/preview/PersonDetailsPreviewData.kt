package com.dezdeqness.feature.details.person.presentation.preview

import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry

object PersonDetailsPreviewData {

    val briefInfo = listOf(
        BriefInfoEntry(title = "Роль", info = "Сэйю"),
        BriefInfoEntry(title = "Дата рождения", info = "26.06.1991"),
        BriefInfoEntry(title = "Японский", info = "花江 夏樹"),
    )

    val sections: List<DetailsSection> = listOf(
        HeaderSection(imageUrl = ""),
        TitleSection(text = "Natsuki Hanae"),
        BriefInfoSection(items = briefInfo),
        BottomSpacerSection,
    )
}
