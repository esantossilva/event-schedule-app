package com.example.weddingapp.framework

import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.EventModel

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

    override fun getScheduleByTitle(title: String): ScheduleModel? = scheduleList.find { it.title == title }

    override fun getScheduleList(): List<ScheduleModel> = scheduleList
}