package com.piggery.app.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    fun parseDate(dateString: String): Long? {
        return try {
            dateFormat.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentDate(): Long {
        return System.currentTimeMillis()
    }

    fun getCalendar(timestamp: Long): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
    }
}
