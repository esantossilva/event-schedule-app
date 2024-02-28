package com.example.domain.models

data class ScheduleModel (
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    var isExclusive: Boolean = false,
)