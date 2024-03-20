package com.example.weddingapp.framework

import android.util.Log
import com.example.domain.models.EventModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

const val ATTR_EVENT_ID = "eventId"
const val ATTR_SCHEDULE = "schedule"
const val ATTR_DATE = "date"
const val ATTR_IS_EXCLUSIVE = "is_exclusive"
const val ATTR_LOCATION = "location"
const val ATTR_TITLE = "title"
const val DATE_FORMAT_PATTERN = "dd.MM"
const val TIME_FORMAT_PATTERN = "HH:mm"
const val TIME_ZONE_ID = "America/Sao_Paulo"

internal fun DocumentSnapshot.getMainEvent() =
    EventModel(
        this[ATTR_TITLE].toString(),
        getDateStringFormatted(this[ATTR_DATE] as Timestamp),
        getTimeStringFormatted(this[ATTR_DATE] as Timestamp),
        this[ATTR_LOCATION].toString()
    )

internal fun DocumentSnapshot.toScheduleList(): List<EventModel> {
    return parseScheduleList(this[ATTR_SCHEDULE])
}

internal fun DocumentSnapshot.isMainEventComplete(): Boolean =
    this.containsAttributes(
        listOf(
            ATTR_TITLE,
            ATTR_DATE,
            ATTR_LOCATION,
        )
    )

internal fun DocumentSnapshot.isScheduleComplete(): Boolean =
    this.containsAttributes(
        listOf(
            ATTR_EVENT_ID,
            ATTR_SCHEDULE,
        )
    )

private fun parseScheduleList(rawData: Any?): List<EventModel> {
    rawData as List<*>

    val scheduleList = mutableListOf<EventModel>()
    rawData.forEach { subList ->
        subList as HashMap<*, *>

        scheduleList.add(
            EventModel(
                subList[ATTR_TITLE].toString(),
                getDateStringFormatted(subList[ATTR_DATE] as Timestamp),
                getTimeStringFormatted(subList[ATTR_DATE] as Timestamp),
                subList[ATTR_LOCATION].toString(),
                (subList[ATTR_IS_EXCLUSIVE] as Boolean)
            )
        )
    }

    return scheduleList
}

private fun DocumentSnapshot.containsAttributes(attributesList: List<String>): Boolean {
    attributesList.forEach { if (!this.contains(it)) return false }
    return true
}

private fun getTimeStringFormatted(timestamp: Timestamp):String {
    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val tz = ZoneId.of(TIME_ZONE_ID)
    val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), tz)
    return DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN).format(time)
}

private fun getDateStringFormatted(timestamp: Timestamp) =
    SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US).format(timestamp.toDate())
