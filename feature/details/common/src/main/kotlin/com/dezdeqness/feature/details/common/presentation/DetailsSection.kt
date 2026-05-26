package com.dezdeqness.feature.details.common.presentation

interface DetailsSection {
    val rendererType: String
    fun key(index: Int) = rendererType + index
}
