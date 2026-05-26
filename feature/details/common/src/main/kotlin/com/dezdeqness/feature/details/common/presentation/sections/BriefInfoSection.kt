package com.dezdeqness.feature.details.common.presentation.sections

import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry

data class BriefInfoSection(val items: List<BriefInfoEntry>) : DetailsSection {
    override val rendererType: String = TYPE
    companion object {
        const val TYPE = "brief_info"
    }
}
