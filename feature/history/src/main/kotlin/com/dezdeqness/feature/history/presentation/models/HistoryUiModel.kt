package com.dezdeqness.feature.history.presentation.models

sealed class HistoryModel {
    abstract fun id(): String

    data class HistoryUiModel(
        val name: String,
        val action: String,
        val imageUrl: String,
    ) : HistoryModel() {
        override fun id() = name + action
    }

    data class HistoryHeaderUiModel(
        val header: String,
    ) : HistoryModel() {
        override fun id() = header
    }

}
