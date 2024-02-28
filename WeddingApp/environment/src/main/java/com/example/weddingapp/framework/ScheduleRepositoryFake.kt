package com.example.weddingapp.framework

import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.ScheduleModel

class ScheduleRepositoryFake : ScheduleInteractor {

    private companion object {
        val scheduleList = listOf(
            ScheduleModel("Maceió", "13.11", "15h", "Taverna do Fabrizzio"),
            ScheduleModel("Churros", "13.11", "19h", "Beira Mar"),
            ScheduleModel("Lagoa", "14.11", "9h", "Lagoa N"),
            ScheduleModel("Caranguejo", "14.11", "18h", "Barraca da Mazé"),
            ScheduleModel("Jantar", "15.11", "19h", "Casa da Vó", true),
        )
    }

    override fun getMainEvent(): ScheduleModel {
        return ScheduleModel("Casamento", "16.11", "15h", "Capela N.Sª de Lourdes")
    }

    override fun getScheduleByTitle(title: String): ScheduleModel? = scheduleList.find { it.title == title }

    override fun getScheduleList(): List<ScheduleModel> = scheduleList
}