package com.dezdeqness.feature.details.common.presentation.sections

import com.dezdeqness.feature.details.common.presentation.DetailsSection

data object BottomSpacerSection : DetailsSection {
    override val rendererType: String = TYPE
    const val TYPE = "bottom_spacer"
}
