package com.example.presenter

import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.ScheduleModel

class MainPresenter(
    private var view: View?,
    private val scheduleDb: ScheduleInteractor,
) : BasePresenter() {
    interface View {
        fun setSchedule(mainEvent:ScheduleModel, scheduleList: List<ScheduleModel>)
    }

    override suspend fun onCreate() {
        view?.setSchedule(
            runInBackground { this.scheduleDb.getMainEvent() },
            runInBackground { this.scheduleDb.getScheduleList() },
        )
    }

    override suspend fun onStart() {}

    override suspend fun onStop() {}

    override suspend fun onDestroy() {}
}