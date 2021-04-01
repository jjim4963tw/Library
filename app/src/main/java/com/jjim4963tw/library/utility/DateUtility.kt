package com.jjim4963tw.library.utility

import java.text.SimpleDateFormat
import java.util.*

class DateUtility {
    val DATE_FORMAT_DASH = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val DATE_FORMAT_SLASH = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val DATA_AND_TIME_DASH = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val DATA_AND_TIME_SLASH = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    fun isAFewSecondsAgo(diff: Long) : Boolean = (diff <= 59000)

    fun isAFewMinutesAgo(diff: Long) : Boolean = (diff <= 600000)

}