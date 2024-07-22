package com.dezdeqness.data.core

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TimestampConverter @Inject constructor() {

    private val timeStampFormatterWithTime =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    private val timeStampFormatter =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun convertToTimeStampWithTime(value: String?): Long {
        return timeStampFormatterWithTime.parse(value.orEmpty())?.time ?: 0L
    }

    fun convertToTimeStamp(value: String?): Long {
        if (value.isNullOrEmpty()) {
            return 0L
        }
        return timeStampFormatter.parse(value)?.time ?: 0L
    }

}
