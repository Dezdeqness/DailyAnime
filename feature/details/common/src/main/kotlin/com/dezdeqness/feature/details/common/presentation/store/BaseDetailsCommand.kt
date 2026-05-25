package com.dezdeqness.feature.details.common.presentation.store

sealed interface BaseDetailsCommand {
    data class LoadDetails(val id: Long) : BaseDetailsCommand
}
