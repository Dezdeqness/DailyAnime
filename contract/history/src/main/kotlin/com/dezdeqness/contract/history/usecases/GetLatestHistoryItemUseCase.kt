package com.dezdeqness.contract.history.usecases

import com.dezdeqness.contract.history.model.HistoryEntity

interface GetLatestHistoryItemUseCase {

    operator fun invoke(): Result<HistoryEntity?>
}
