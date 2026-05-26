package com.dezdeqness.feature.details.common.presentation.sections

import com.dezdeqness.feature.details.common.presentation.DetailsSection

data class HeaderSection(
    val imageUrl: String,
    val rating: Float? = null,
) : DetailsSection {
    override val rendererType: String = TYPE
    override fun key(index: Int) = super.key(index) + rating

    companion object {
        const val TYPE = "header"
    }
}
