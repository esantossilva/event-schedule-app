package com.example.domain.interactors

import com.example.domain.models.ScheduleModel

interface ScheduleInteractor {
    fun getMainEvent(): ScheduleModel
    fun getScheduleByTitle(title: String): ScheduleModel?
    fun getScheduleList(): List<ScheduleModel>
}