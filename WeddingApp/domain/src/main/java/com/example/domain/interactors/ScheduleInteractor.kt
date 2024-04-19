package com.example.domain.interactors

import com.example.domain.models.EventModel
import com.example.domain.utils.Outcome

enum class ErrorType {
    NOT_FOUND,
    CONNECTIVITY,
    GENERIC,
}

interface ScheduleInteractor {
    fun getMainEvent(eventId: String): Outcome<EventModel, ErrorType>
    fun getEventList(eventId: String): Outcome<List<EventModel>, ErrorType>
}