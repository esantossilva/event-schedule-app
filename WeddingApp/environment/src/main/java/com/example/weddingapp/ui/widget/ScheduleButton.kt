package com.example.weddingapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.domain.models.EventModel
import com.example.weddingapp.R
import com.example.weddingapp.databinding.ItemScheduleButtonBinding
import com.example.weddingapp.ui.setVisible

class ScheduleButton(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    interface DisableButtonCallback {
        fun onDisableOtherButtons(activeButtonId: Int)
    }

    private var binding:ItemScheduleButtonBinding
    private var isActive: Boolean = false

    init {
        this.binding = ItemScheduleButtonBinding.inflate(LayoutInflater.from(context), this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ScheduleButton,
            0, 0).apply {

            try {
                this@ScheduleButton.binding.apply {
                    tvLabel.text = getString(R.styleable.ScheduleButton_label)
                    tvDate.text = getString(R.styleable.ScheduleButton_date)
                    tvTime.text = getString(R.styleable.ScheduleButton_time)
                    tvLocation.text = getString(R.styleable.ScheduleButton_location)
                }
            } finally {
                recycle()
            }
        }

        setDetailsVisibility()
    }

    fun setDetailsVisibility(visible: Boolean = false) {
        isActive = visible
        this.binding.clDetails.setVisible(visible, useGone = true)
    }

    fun setupButton(eventModel: EventModel, callback: DisableButtonCallback) {
        this.binding.apply {
            tvLabel.text = eventModel.title
            tvDate.text = eventModel.date
            tvTime.text = eventModel.time
            tvLocation.text = eventModel.location

            clButtonContainer.setOnClickListener {
                isActive = !isActive
                callback.onDisableOtherButtons(this@ScheduleButton.id)
                setButtonSize()
                setDetailsVisibility(isActive)
            }
        }
    }

    private fun setButtonSize() {
        this.binding.tvLabel.layoutParams?.height =
            if (isActive) ViewGroup.LayoutParams.WRAP_CONTENT
            else context.resources.getDimension(R.dimen.minimum_button_size).toInt()
    }
}
