package com.example.weddingapp.ui

import android.widget.Toast
import com.example.domain.models.EventModel
import com.example.presenter.MainPresenter
import com.example.weddingapp.databinding.ActivityMainBinding
import com.example.weddingapp.framework.ScheduleRepositoryFirebase
import com.example.weddingapp.ui.widget.ScheduleButton

class MainActivity : BaseActivity<ActivityMainBinding>(), MainPresenter.View {

    private val presenter: MainPresenter = MainPresenter(
        this,
        ScheduleRepositoryFirebase(this),
    )
    private lateinit var disableButtonCallback: ScheduleButton.DisableButtonCallback

    init {
        bindPresenterToLifecycle(this.presenter)
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate() {
        setupEventListeners()
    }

    override fun setMainEvent(mainEvent: EventModel) {
        this.binding.lSchedule.sbWedding.setupButton(mainEvent, disableButtonCallback)
    }

    override fun setSchedule(scheduleList: List<EventModel>) {
        this.binding.lSchedule.apply {
            clSchedule.addScheduleViews(
                this@MainActivity,
                scheduleList,
                ivScheduleTrail,
                disableButtonCallback
            )
        }
    }

    private fun setupEventListeners() {
        this.binding.apply {
            hbTours.setOnClickListener { testMessage("Tours") }
            hbLocation.setOnClickListener { testMessage("Location") }
            hbAccommodation.setOnClickListener { testMessage("Accommodation") }

            hbGifts.setOnClickListener { testMessage("Gifts") }
            hbConfirmation.setOnClickListener { testMessage("Confirmation") }
            hbMakeup.setOnClickListener { testMessage("Makeup") }

            disableButtonCallback = object : ScheduleButton.DisableButtonCallback {
                override fun onDisableOtherButtons(activeButtonId: Int) {
                    lSchedule.clSchedule.disableAllScheduleDetails(activeButtonId)
                }
            }
        }
    }

    private fun testMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}