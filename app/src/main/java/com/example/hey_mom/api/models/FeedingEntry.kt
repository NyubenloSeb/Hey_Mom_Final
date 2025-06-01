package com.example.hey_mom.api.models

data class FeedingEntry(
    val feeding_id: Int,
    val baby_id: Int,
    val feeding_time: String,
    val feeding_type: String,
    val quantity_ml: Int,
    val notes: String?
)
