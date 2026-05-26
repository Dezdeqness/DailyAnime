package com.dezdeqness.feature.details.common.presentation.sections

import com.dezdeqness.feature.details.common.presentation.DetailsSection

data class TitleSection(val text: String) : DetailsSection {
    override val rendererType: String = TYPE
    companion object {
        const val TYPE = "title"
    }
}
