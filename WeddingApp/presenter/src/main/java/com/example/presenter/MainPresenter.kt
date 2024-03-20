package com.example.presenter

import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.EventModel
import com.example.domain.utils.Outcome

class MainPresenter(
    private var view: View?,
    private val scheduleDb: ScheduleInteractor,
) : BasePresenter() {
    interface View {
        fun setMainEvent(mainEvent:EventModel)
        fun setSchedule(scheduleList: List<EventModel>)
    }

    private companion object {
        const val EVENT_ID = "wed-janet_barry"
    }

    override suspend fun onCreate() {

        runInBackground {
            val mainEvent = when (val scheduleRepositoryResult = this.scheduleDb.getMainEvent(EVENT_ID)) {
                is Outcome.Success -> scheduleRepositoryResult.value
                is Outcome.Failure -> {
                    null
                }
            }

            val scheduleList = when (val scheduleRepositoryResult = this.scheduleDb.getEventList(EVENT_ID)) {
                is Outcome.Success -> scheduleRepositoryResult.value
                is Outcome.Failure -> {
                    null
                }
            }

            scheduleList?.let {
                enqueueOnMain { view?.setSchedule(it) }
            }

            mainEvent?.let {
                enqueueOnMain { view?.setMainEvent(it) }
            }
        }
    }

    override suspend fun onStart() {}

    override suspend fun onStop() {}

    override suspend fun onDestroy() {}
}