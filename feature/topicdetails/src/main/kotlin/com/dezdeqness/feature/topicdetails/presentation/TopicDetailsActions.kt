package com.dezdeqness.feature.topicdetails.presentation

import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel

interface TopicDetailsActions {
    fun onBackPressed()
    fun onPullDownRefreshed()
    fun onLinkedEntityClicked(entity: LinkedEntityUiModel)
    fun onVideoClicked(url: String)
}
