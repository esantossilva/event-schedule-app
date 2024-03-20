package com.example.weddingapp.ui

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.allViews
import com.example.domain.models.ConnectConstraint
import com.example.domain.models.ConstraintInstructions
import com.example.domain.models.DisconnectConstraint
import com.example.domain.models.EventModel
import com.example.weddingapp.ui.widget.ScheduleButton

internal fun ConstraintLayout.addScheduleViews(
    context: Context,
    scheduleList: List<EventModel>,
    horizontalConstraint: View,
    hideAllDetailsCallback: ScheduleButton.DisableButtonCallback
) {
    val viewIdList: MutableList<Int> = mutableListOf()
    scheduleList.apply {
        forEach { _ -> viewIdList.add(View.generateViewId()) }
        forEachIndexed { index, schedule ->
            this@addScheduleViews.addView(
                ScheduleButton(context).apply {
                    id = viewIdList[index]
                    setupButton(schedule, hideAllDetailsCallback)
                }
            )
            connectConstraint(
                this@addScheduleViews,
                viewIdList[index],
                index,
                scheduleList.lastIndex,
                horizontalConstraint,
                if (index > 0) viewIdList[index - 1] else 0,
                if (index < scheduleList.lastIndex) viewIdList[index + 1] else scheduleList.lastIndex,
            )
        }
    }
}

internal fun ConstraintLayout.disableAllScheduleDetails(activeButtonId: Int) {
    this.allViews.forEach {
        if (it is ScheduleButton && it.id != activeButtonId)
            it.setDetailsVisibility()
    }
}

internal fun connectConstraint(
    constraintLayout: ConstraintLayout,
    id: Int,
    index: Int,
    maxIndex: Int,
    viewToConstraint: View,
    topConstraintId: Int,
    bottomConstraintId: Int,
) {
    val constraintStart = if (index % 2 == 0) ConstraintSet.END else ConstraintSet.START
    val constraintEnd = if (index % 2 == 0) ConstraintSet.START else ConstraintSet.END

    val constraintList = listOf(
        if (index == 0) ConnectConstraint(id, ConstraintSet.TOP, viewToConstraint.id, ConstraintSet.TOP)
        else ConnectConstraint(id, ConstraintSet.TOP, topConstraintId, ConstraintSet.BOTTOM),
        if (index == maxIndex) ConnectConstraint(id, ConstraintSet.BOTTOM, viewToConstraint.id, ConstraintSet.BOTTOM)
        else ConnectConstraint(id, ConstraintSet.BOTTOM, bottomConstraintId, ConstraintSet.TOP),
        ConnectConstraint(id, constraintStart, viewToConstraint.id, constraintEnd),
    )
    constraintLayout.updateConstraints(constraintList)
}

internal fun ConstraintLayout.updateConstraints(instructions: List<ConstraintInstructions>) {
    ConstraintSet().also {
        it.clone(this)
        for (instruction in instructions) {
            if (instruction is ConnectConstraint) it.connect(instruction.startID, instruction.startSide, instruction.endID, instruction.endSide)
            if (instruction is DisconnectConstraint) it.clear(instruction.startID, instruction.startSide)
        }
        it.applyTo(this)
    }
}

fun View.setVisible(visible: Boolean, useGone: Boolean = false) {
    visibility = when {
        visible -> View.VISIBLE
        useGone -> View.GONE
        else -> View.INVISIBLE
    }
}
