package com.dezdeqness.feature.history.presentation.preview

import com.dezdeqness.feature.history.presentation.models.HistoryModel

object HistoryPreviewData {

    val item = HistoryModel.HistoryUiModel(
        name = "Атака титанов",
        action = "Просмотрено 12 эпизод",
        imageUrl = "",
    )

    val header = "Сегодня"

    val list = listOf(
        HistoryModel.HistoryHeaderUiModel(header = "Сегодня"),
        HistoryModel.HistoryUiModel(
            name = "Атака титанов: Финал",
            action = "Просмотрено 12 эпизод",
            imageUrl = "",
        ),
        HistoryModel.HistoryUiModel(
            name = "Магическая битва",
            action = "Просмотрено 5 эпизод",
            imageUrl = "",
        ),
        HistoryModel.HistoryHeaderUiModel(header = "Вчера"),
        HistoryModel.HistoryUiModel(
            name = "Ван-Пис",
            action = "Просмотрено 1089 эпизод",
            imageUrl = "",
        ),
        HistoryModel.HistoryUiModel(
            name = "Наруто: Ураганные хроники",
            action = "Добавлено в список",
            imageUrl = "",
        ),
    )
}
