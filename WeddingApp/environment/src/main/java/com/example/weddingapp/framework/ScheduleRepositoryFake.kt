package com.example.weddingapp.framework

import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.ScheduleModel

class ScheduleRepositoryFake : ScheduleInteractor {

    private companion object {
        val scheduleList = listOf(
            ScheduleModel("Beach day", "26.07", "9h", "Porto de Galinhas"),
            ScheduleModel("Drinks at the Rooftop", "27.07", "19h", "Ruffo Rooftop"),
            ScheduleModel("Boat Party", "28.07", "13h", "Pier"),
            ScheduleModel("Family Dinner", "28.07", "21h", "Restaurant", true),
        )
    }

    override fun getMainEvent(): ScheduleModel {
        return ScheduleModel("Wedding", "29.07", "15h", "Madre de Deus Church")
    }

    override fun getScheduleByTitle(title: String): ScheduleModel? = scheduleList.find { it.title == title }

    override fun getScheduleList(): List<ScheduleModel> = scheduleList
}