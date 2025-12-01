package com.dezdeqness.data.core

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TimestampConverter @Inject constructor() {

    private val timeStampFormatterWithTime =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    private val timeStampFormatterWithTimeNoMillis =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

    private val timeStampFormatter =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun convertToTimeStampWithTime(value: String?): Long {
        if (value.isNullOrEmpty()) {
            return 0L
        }

        return try {
            timeStampFormatterWithTime.parse(value)?.time ?: 0L
        } catch (e: Exception) {
            try {
                timeStampFormatterWithTimeNoMillis.parse(value)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }
    }

    fun convertToTimeStamp(value: String?): Long {
        if (value.isNullOrEmpty()) {
            return 0L
        }
        return timeStampFormatter.parse(value)?.time ?: 0L
    }

}
