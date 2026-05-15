package com.dezdeqness.feature.topicdetails.presentation.models

sealed interface LinkedEntityState {
    data object Initial : LinkedEntityState
    data object Loading : LinkedEntityState
    data object Error : LinkedEntityState
    data object Empty : LinkedEntityState
    data class Loaded(val entity: LinkedEntityUiModel) : LinkedEntityState
}
