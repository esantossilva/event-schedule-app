package com.example.weddingapp.framework

import com.example.domain.interactors.ErrorType
import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.EventModel
import com.example.domain.utils.Outcome

class ScheduleRepositoryFake : ScheduleInteractor {

    private companion object {
        val scheduleList = listOf(
            EventModel("Beach day", "26.07", "9h", "Porto de Galinhas"),
            EventModel("Drinks at the Rooftop", "27.07", "19h", "Ruffo Rooftop"),
            EventModel("Boat Party", "28.07", "13h", "Pier"),
            EventModel("Family Dinner", "28.07", "21h", "Restaurant", true),
        )
    }

    override fun getMainEvent(eventId: String): Outcome<EventModel, ErrorType> {
        return Outcome.Success(EventModel("Wedding", "29.07", "15h", "Madre de Deus Church"))
    }

    override fun getEventList(eventId: String): Outcome<List<EventModel>, ErrorType> =
        Outcome.Success(scheduleList)
}