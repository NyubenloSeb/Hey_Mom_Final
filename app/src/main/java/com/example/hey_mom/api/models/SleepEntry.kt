package com.example.hey_mom.api.models

data class SleepEntry(
    val sleep_id: Int,
    val baby_id: Int,
    val sleep_start: String,
    val sleep_end: String,
    val notes: String
)
