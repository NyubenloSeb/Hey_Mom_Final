package com.example.hey_mom.api.models

data class DiaperChange(
    val diaper_ch_id: Int,
    val baby_id: Int,
    val change_time: String,
    val condition: String,
    val notes: String?
)
