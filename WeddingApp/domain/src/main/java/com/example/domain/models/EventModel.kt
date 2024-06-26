package com.example.domain.models

data class EventModel (
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    var isExclusive: Boolean = false,
)