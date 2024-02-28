package com.example.weddingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.allViews
import com.example.domain.models.ScheduleModel
import com.example.presenter.MainPresenter
import com.example.weddingapp.R
import com.example.weddingapp.databinding.ActivityMainBinding
import com.example.weddingapp.framework.ScheduleRepositoryFake
import com.example.weddingapp.ui.widget.ScheduleButton

class MainActivity : BaseActivity<ActivityMainBinding>(), MainPresenter.View {

    private val presenter: MainPresenter

    init {
        this.presenter = MainPresenter(
            this,
            ScheduleRepositoryFake(),
        )
        bindPresenterToLifecycle(this.presenter)
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate() {}

    override fun setSchedule(mainEvent: ScheduleModel, scheduleList: List<ScheduleModel>) {
        this.binding.lSchedule.apply {
            val disableButtonCallback = object : ScheduleButton.DisableButtonCallback {
                override fun onDisableOtherButtons(activeButtonId: Int) {
                    clSchedule.disableAllScheduleDetails(activeButtonId)
                }
            }

            clSchedule.addScheduleViews(
                this@MainActivity,
                scheduleList,
                ivScheduleTrail,
                disableButtonCallback
            )
            sbWedding.setupButton(mainEvent, disableButtonCallback)
        }
    }

    private fun setupEventListeners() {
        this.binding.apply {
            hbTours.setOnClickListener {  }
            hbLocation.setOnClickListener {  }
            hbAccommodation.setOnClickListener {  }

            hbGifts.setOnClickListener {  }
            hbConfirmation.setOnClickListener {  }
            hbMakeup.setOnClickListener {  }
        }
    }
}